{{- define "logstash.name" -}}
logstash
{{- end -}}

{{- define "logstash.fullname" -}}
{{ include "logstash.name" . }}-{{ .Release.Name }}
{{- end -}}

{{- define "logstash.chart" -}}
logstash-0.1.0
{{- end -}} 