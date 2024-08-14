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
public class EnglishStringTable implements StringTable {
    @Override
    public String applicationName() {
        return "Application Name";
    }

    @Override
    public String key() {
        return "Key";
    }

    @Override
    public String value() {
        return "Value";
    }

    @Override
    public String addConfig() {
        return "Add Configuration";
    }

    @Override
    public String htmlLanguage() {
        return "en-US";
    }

    @Override
    public String operationSubmitted() {
        return "The operation was successfully submitted.";
    }
}
