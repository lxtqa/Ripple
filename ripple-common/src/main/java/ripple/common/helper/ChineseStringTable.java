// Copyright (c) 2024 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.common.helper;

/**
 * @author Zhen Tang
 */
public class ChineseStringTable implements StringTable {
    @Override
    public String applicationName() {
        return "应用名称";
    }

    @Override
    public String key() {
        return "键";
    }

    @Override
    public String value() {
        return "值";
    }

    @Override
    public String addConfig() {
        return "添加配置";
    }

    @Override
    public String htmlLanguage() {
        return "zh-CN";
    }

    @Override
    public String operationSubmitted() {
        return "操作已成功提交。";
    }
}
