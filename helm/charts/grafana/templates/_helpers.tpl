{{- define "grafana.name" -}}
grafana
{{- end -}}

{{- define "grafana.fullname" -}}
{{ include "grafana.name" . }}-{{ .Release.Name }}
{{- end -}}

{{- define "grafana.chart" -}}
grafana-0.1.0
{{- end -}} 