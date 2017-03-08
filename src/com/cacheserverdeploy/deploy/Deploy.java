package com.cacheserverdeploy.deploy;


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
    	String[] totalInfo = graphContent[0].split(" ");
    	int consumerNode = Integer.parseInt(totalInfo[2]);
    	String[] result = new String[consumerNode+2];
    	result[0] = consumerNode+"";
    	result[1] = "\r\n";
    	
    	int index = 2;
    	int i = graphContent.length - 1;
    	String tmp = "-1";
    	while(!(tmp = graphContent[i--]).equals("")){
    		String[] consumerInfo = tmp.split(" ");
    		result[index++] = consumerInfo[1] + " " + consumerInfo[0] + " " + consumerInfo[2];
    	}
    	
        /**do your work here**/
        return result;
    }

}
