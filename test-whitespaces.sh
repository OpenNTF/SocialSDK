#!/bin/bash
git grep -I --files-with-matches '' -- */* ':(exclude)*/*mock' || exit 0;
exit 1; 

