# Code Style

В проекте используется detekt и [ktlint](https://detekt.dev/docs/rules/formatting/)-плагин для него. 
Ktlint это чуть более строгие стандартные [котлиновские гайдлайны](https://kotlinlang.org/docs/coding-conventions.html). 

И детект и форматтер запускаются одной командой `./gradlew detekt` — форматтер постарается сразу исправить то, что сможет, но ошибки покажет в любом случае, даже если все исправил.  

Чтобы detekt запускался перед каждым коммитом, выполните скрипт `sudo bash scripts/install-git-hooks.sh` — может понадобиться sudo так как гит-хуки это исполняемые файлы.
