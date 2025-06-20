{{- define "zipkin.name" -}}
zipkin
{{- end -}}

{{- define "zipkin.fullname" -}}
{{ include "zipkin.name" . }}-{{ .Release.Name }}
{{- end -}}

{{- define "zipkin.chart" -}}
zipkin-0.1.0
{{- end -}} 