{{- define "elasticsearch.name" -}}
elasticsearch
{{- end -}}

{{- define "elasticsearch.fullname" -}}
{{ include "elasticsearch.name" . }}-{{ .Release.Name }}
{{- end -}}

{{- define "elasticsearch.chart" -}}
elasticsearch-0.1.0
{{- end -}} 