#!/bin/bash

# Скрипт для развертывания Apache Kafka в Kubernetes
# Использование: ./deploy-kafka.sh [dev|test|prod] <BUILD_NUMBER>

set -e

# Цвета для вывода
RED='\\033[0;31m'
GREEN='\\033[0;32m'
YELLOW='\\033[1;33m'
NC='\\033[0m' # No Color

# Функция для вывода сообщений
log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Проверка аргументов
if [ "$#" -ne 2 ]; then
    log_error "Необходимо указать окружение (dev, test или prod) и номер сборки (BUILD_NUMBER)"
    echo "Использование: $0 [dev|test|prod] <BUILD_NUMBER>"
    exit 1
fi

ENVIRONMENT=$1
BUILD_NUMBER=$2
HELM_CHART_DIR="helm"
VALUES_FILE=""

case $ENVIRONMENT in
    dev)
        VALUES_FILE="values-dev.yaml"
        NAMESPACE="dev"
        log_info "Развертывание в dev окружении"
        ;;
    test)
        VALUES_FILE="values-test.yaml"
        NAMESPACE="test"
        log_info "Развертывание в test окружении"
        ;;
    prod)
        VALUES_FILE="values-prod.yaml"
        NAMESPACE="prod"
        log_warn "Развертывание в PRODUCTION окружении"
        ;;
    *)
        log_error "Неверное окружение: $ENVIRONMENT"
        echo "Допустимые значения: dev, test, prod"
        exit 1
        ;;
esac

# Проверка наличия Helm
if ! command -v helm &> /dev/null; then
    log_error "Helm не установлен. Установите Helm и попробуйте снова."
    exit 1
fi

# Проверка наличия kubectl
if ! command -v kubectl &> /dev/null; then
    log_error "kubectl не установлен. Установите kubectl и попробуйте снова."
    exit 1
fi

# Проверка подключения к кластеру
if ! kubectl cluster-info &> /dev/null; then
    log_error "Не удается подключиться к кластеру Kubernetes"
    exit 1
fi

log_info "Проверка подключения к кластеру Kubernetes..."
kubectl cluster-info

# Создание namespace если не существует
log_info "Создание namespace $NAMESPACE..."
kubectl create namespace $NAMESPACE --dry-run=client -o yaml | kubectl apply -f -

# Обновление зависимостей Helm (относительно корня проекта)
log_info "Обновление зависимостей Helm..."
helm dependency update "./$HELM_CHART_DIR"

# Обновление значений тегов в values.yaml для всех микросервисов
log_info "Обновление тегов образов в values.yaml на: ${BUILD_NUMBER}"
find "./$HELM_CHART_DIR/charts" -maxdepth 2 -type d -name "*-service" -print0 | while IFS= read -r -d $'\0' dir; do
    SERVICE_NAME=$(basename "$dir")
    VALUES_FILE_PATH="$dir/values.yaml"
    if [ -f "$VALUES_FILE_PATH" ]; then
        # Используем Perl для in-place замены, совместимой с macOS и Linux
        perl -pi -e "s/^(  tag: \").*(\")/\$1${BUILD_NUMBER}\$2/" "$VALUES_FILE_PATH"
        log_info "Обновлен tag в ${VALUES_FILE_PATH} для ${SERVICE_NAME} на ${BUILD_NUMBER}"
    else
        log_warn "Файл ${VALUES_FILE_PATH} не найден для ${SERVICE_NAME}"
    fi
done

# Развертывание
log_info "Развертывание банковского приложения с Kafka..."
helm upgrade --install bank-"$ENVIRONMENT" "./$HELM_CHART_DIR" \\
    -f "./$HELM_CHART_DIR/$VALUES_FILE" \\
    -n "$NAMESPACE" \\
    --wait \\
    --timeout=10m

# Проверка статуса развертывания
log_info "Проверка статуса развертывания..."
kubectl get pods -n "$NAMESPACE"

# Проверка Kafka
log_info "Проверка статуса Kafka..."
kubectl get pods -n "$NAMESPACE" -l app.kubernetes.io/name=kafka

# Проверка топиков Kafka
log_info "Ожидание готовности Kafka..."
sleep 30

KAFKA_POD=$(kubectl get pods -n "$NAMESPACE" -l app.kubernetes.io/name=kafka -o jsonpath='{.items[0].metadata.name}')
if [ -n "$KAFKA_POD" ]; then
    log_info "Проверка топиков Kafka..."
    kubectl exec -n "$NAMESPACE" "$KAFKA_POD" -- kafka-topics.sh --bootstrap-server localhost:9092 --list
else
    log_warn "Не удалось найти под Kafka"
fi

log_info "Развертывание завершено успешно!"
log_info "Для просмотра логов используйте: kubectl logs -n $NAMESPACE -l app.kubernetes.io/name=kafka"
log_info "Для подключения к Kafka: kubectl exec -it -n $NAMESPACE $KAFKA_POD -- kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic account-events --from-beginning" 