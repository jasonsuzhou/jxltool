#How to share local project to GitHub
#goto the project folder
git init
git remote add origin https://github.com/jasonsuzhou/jxltool.git
#add all files
git add .
#local commit
git commit -a -m "Initial project"
#do the real commit
git push -u origin --all