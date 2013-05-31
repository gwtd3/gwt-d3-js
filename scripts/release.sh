#!/bin/sh

fetch_versions() {
  wget -q -O - "https://api.github.com/repos/mbostock/d3/git/refs/tags" \
    | grep -oE '"ref"\s*:\s*"refs/tags/v3\.[0-9]+\.[0-9]+"' \
    | sed 's|"ref": "refs/tags/v\(.*\)"|\1|'
}

tag_exists() {
  if [ $(git tag -l "$1" | wc -l) -eq 0 ]
    then
      return 1
    else
      return 0
    fi
}

remove_tag() {
  git tag -d "$1" && git push origin :"$1"
}

release() {
  echo ""
  echo "################################"
  printf "# Releasing Version %-10s #\n" $version
  echo "################################"
  git fetch
  mvn -B -s /private/ericcitaire/.m2/settings.xml \
    -DreleaseVersion="$1" \
    release:clean \
    release:prepare \
    release:perform
}

rollback() {
  mvn -B -s /private/ericcitaire/.m2/settings.xml \
    release:rollback
  remove_tag "$1"
}

rollback_and_die() {
  rollback "$1"
  exit 1
}

cd /tmp
rm -rf gwt-d3js
git clone git@github.com:ericcitaire/gwt-d3js.git
cd gwt-d3js
for version in $(fetch_versions)
  do
    tag="gwt-d3js-$version"
    tag_exists "$tag" || release "$version" || rollback_and_die "$tag"
  done


