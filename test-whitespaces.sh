#!/bin/bash
git grep -I --files-with-matches  -q '' -- */* || exit 0;
exit 1; 

