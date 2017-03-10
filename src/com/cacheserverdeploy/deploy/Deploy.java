package com.cacheserverdeploy.deploy;

import com.huawei.utils.ServerLocate;

public class Deploy
{
    /**
     * 你需要完成的入口
     * <功能详细描述>
     * @param graphContent 用例信息文件
     * @return [参数说明] 输出结果信息
     * @see [类、类#方法、类#成员]
     */
    public static String[] deployServer(String[] graphContent)
    {
    	String[] result = new String[1];
    	ServerLocate.displayPath(graphContent);
        /**do your work here**/
        return result;
    }

}
