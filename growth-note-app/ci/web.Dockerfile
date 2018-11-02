FROM nginx:1.13-alpine

ADD default /etc/nginx/conf.d/default.conf

ADD build /usr/share/nginx/html
