sed -i "" "s#^buidQLiveSH=.*#buidQLiveSH=1#g"  gradle.properties
./gradlew :qlivesdk:makeAAR
sed -i "" "s#^buidQLiveSH=.*#buidQLiveSH=0#g"  gradle.properties