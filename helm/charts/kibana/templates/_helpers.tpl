{{- define "kibana.name" -}}
kibana
{{- end -}}

{{- define "kibana.fullname" -}}
{{ include "kibana.name" . }}-{{ .Release.Name }}
{{- end -}}

{{- define "kibana.chart" -}}
kibana-0.1.0
{{- end -}} 