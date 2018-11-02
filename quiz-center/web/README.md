### quiz-center 页面
 

 git filter-branch --force --index-filter 'git rm -r --cached --ignore-unmatch backend assembly ci docs web/basicQuiz-app web/homework-app web/subjective-app web/ci web/instructor-web' --prune-empty --tag-name-filter cat -- --all