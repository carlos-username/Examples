#!/bin/bash

display_help() {
   echo "Usage: $0 {--files|--all} backup_file_name" >&2
   echo
   echo "   -f, --files archive only files containing timestamp in format: YYYYmmdd"
   echo "   -a, --all archive files and directories containing timestamp in format: YYYYmmdd "
   echo "   type --help or -h to show what can be done"
   echo
   exit 1
}

if [ "$#" -ne 2 ]; then
   echo "make sure to indicate the option and what name the backup file will have"
   echo
   display_help
fi
backup_name=$2


case "$1" in
 -f | --files)
   find . -type f -regextype posix-extended -regex '.*[0-9]{4}[0-9]{2}[0-9]{2}$' -print0 | tar --r
emove-files -czvf $backup_name --null -T -
   ;;
 -a | --all)
   find . -regextype posix-extended -regex '.*[0-9]{4}[0-9]{2}[0-9]{2}$' -print0 | tar --remove-fi
les -czvf $backup_name --null -T -
   ;;
 -h | --help)
     echo "helping menu: "
     display_help
     ;;
 *)
     echo "You have not specified what to do correctly."
     display_help    
  exit 1
   ;;
esac
