#!/usr/bin/env bash
# deploy script for the web front-end

# This file is responsible for preprocessing all TypeScript files, making sure
# all dependencies are up-to-date, and copying all necessary files into the
# web deploy directory.

# This is the resource folder where maven expects to find our files
if [ -z "$TARGET" ]
then
    TARGET=../backend/src/main/resources
fi

# This is the folder that we used with the Spark.staticFileLocation command
WEB=web
IMAGE=images

# step 1: make sure we have someplace to put everything.  We will delete the
#         old folder tree, and then make it from scratch
rm -rf $TARGET
mkdir $TARGET
mkdir $TARGET/$WEB
mkdir $TARGET/$WEB/$IMAGE

# there are many more steps to be done.  For now, we will just copy an HTML file
cp index.html tasks.html taskPage.html tasksAddForm.html app.css style.css node_modules/jquery/dist/jquery.min.js allProjectsPage.html allProjectsPage.js $TARGET/$WEB

cp images/logo.png $TARGET/$WEB/$IMAGE
cp images/logoWhite.png $TARGET/$WEB/$IMAGE
cp images/uploadwhite.png $TARGET/$WEB/$IMAGE
cp images/add-list.png $TARGET/$WEB/$IMAGE
cp images/newproject.png $TARGET/$WEB/$IMAGE
cp images/project.png $TARGET/$WEB/$IMAGE
cp googleSignIn.js $TARGET/$WEB


#Put Client Secret Doc into Resources Folder
cp ../backend/StoplightCS.json ../backend/src/main/resources/StoplightCS.json

# step 4: compile TypeScript files
node_modules/.bin/tsc app.ts --strict --outFile $TARGET/app.js
node_modules/.bin/tsc tasks.ts --strict --outFile $TARGET/tasks.js
node_modules/.bin/tsc files.ts --strict --outFile $TARGET/files.js


# step 4: compile TypeScript files
node_modules/typescript/bin/tsc app.ts --strict --outFile $TARGET/$WEB/app.js
node_modules/typescript/bin/tsc tasks.ts --strict --outFile $TARGET/$WEB/tasks.js
node_modules/typescript/bin/tsc task.ts --strict --outFile $TARGET/$WEB/task.js
node_modules/typescript/bin/tsc projects.ts --strict --outFile $TARGET/$WEB/projects.js
node_modules/typescript/bin/tsc files.ts --strict --outFile $TARGET/$WEB/files.js
node_modules/typescript/bin/tsc subtasks.ts --strict --outFile $TARGET/$WEB/subtasks.js
node_modules/typescript/bin/tsc messages.ts --strict --outFile $TARGET/$WEB/messages.js
