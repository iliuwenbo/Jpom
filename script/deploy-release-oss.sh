#!/bin/bash
#
# Copyright (c) 2019 Of Him Code Technology Studio
# Jpom is licensed under Mulan PSL v2.
# You can use this software according to the terms and conditions of the Mulan PSL v2.
# You may obtain a copy of Mulan PSL v2 at:
# 			http://license.coscl.org.cn/MulanPSL2
# THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
# See the Mulan PSL v2 for more details.
#


current_path=$(pwd)
case "$(uname)" in
    Linux)
		bin_abs_path=$(readlink -f $(dirname "$0"))
		;;
	*)
		bin_abs_path=`cd $(dirname $0) || exit; pwd`
		;;
esac
DIR_PATH=${bin_abs_path}/../jpom-parent/

now_version="$(cat "${DIR_PATH}/docs/version.txt")"

#
now_version="$(echo $now_version |tr -d '\n\r')"

echo "当前路径：${current_path} 脚本路径：${bin_abs_path},目录：${DIR_PATH} 当前版本：${now_version}"

if [ ! -n "$now_version" ]; then
    echo "ERROR: 版本不存在，请确认 /jpom-parent/docs/version.txt 中信息正确"
    exit
fi

OS="$(uname)"
case "${OS}" in
Linux)
	ossUtil='ossutil64'
	;;
Darwin)
	ossUtil='ossutilmac64'
	;;
*)
	echo "Unsupported os: ${OS}"
	exit 1
	;;
esac

echo  "${OS} ${ossUtil} 开始上传"

# 同步到 agent oss 中
${ossUtil} cp ${DIR_PATH}/modules/agent/target/agent-${now_version}-release.tar.gz oss://jpom-releases/release/${now_version}/ -f
${ossUtil} cp ${DIR_PATH}/modules/agent/target/agent-${now_version}-release.tar.gz.sha1 oss://jpom-releases/release/${now_version}/ -f
${ossUtil} cp ${DIR_PATH}/modules/agent/target/agent-${now_version}-release.zip oss://jpom-releases/release/${now_version}/ -f
${ossUtil} cp ${DIR_PATH}/modules/agent/target/agent-${now_version}-release.zip.sha1 oss://jpom-releases/release/${now_version}/ -f

# 同步到 server oss 中
${ossUtil} cp ${DIR_PATH}/modules/server/target/server-${now_version}-release.tar.gz oss://jpom-releases/release/${now_version}/ -f
${ossUtil} cp ${DIR_PATH}/modules/server/target/server-${now_version}-release.tar.gz.sha1 oss://jpom-releases/release/${now_version}/ -f
${ossUtil} cp ${DIR_PATH}/modules/server/target/server-${now_version}-release.zip oss://jpom-releases/release/${now_version}/ -f
${ossUtil} cp ${DIR_PATH}/modules/server/target/server-${now_version}-release.zip.sha1 oss://jpom-releases/release/${now_version}/ -f

rm -f ${DIR_PATH}/jpom-${now_version}.zip

zip -rj ${DIR_PATH}/jpom-${now_version}.zip ${DIR_PATH}/modules/server/target/server-${now_version}-release.zip ${DIR_PATH}/modules/agent/target/agent-${now_version}-release.zip

# 完整的压缩包
${ossUtil} cp ${DIR_PATH}/jpom-${now_version}.zip oss://jpom-releases/release/${now_version}/ -f

rm -f ${DIR_PATH}/jpom-${now_version}.zip
