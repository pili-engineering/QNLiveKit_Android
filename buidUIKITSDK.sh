echo "编译qliveUIkit sdk"
echo "----------------------"
sed -i "" "s#^buidUIKitSH=.*#buidUIKitSH=1#g"  gradle.properties
./gradlew :liveroom-uikit:makeAAR
sed -i "" "s#^buidUIKitSH=.*#buidUIKitSH=0#g"  gradle.properties