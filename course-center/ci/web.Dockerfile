FROM nginx:1.13-alpine

ADD default /etc/nginx/conf.d/default.conf

ADD basicQuiz /usr/share/nginx/html/basicQuiz
ADD subjectiveQuiz /usr/share/nginx/html/subjectiveQuiz
