          _     _____________   
         | |   |_____________| 
         | |         | |
         | |         | |
         | |         | |
         | |         | |
         / |         | |                                                   
    ____/ /          | |
    \____/           |_|

---
、、、
This is a maven project.
This tool used to import and export 2003 excel more easily.
Based on configuration, this tool is very flexible。
This tool depends on below jar file(you can also find inside pom.xml):
	- dom4j.jar                          version : 1.6.1
	- jxl.jar                            version : 2.6.12
	- org.apache.commons.lang3.jar       version : 3.1
、、、
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