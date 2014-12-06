package com.zlzkj.core.sql;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.zlzkj.core.util.Fn;

/**
 * sql查询语句生成器，用于简化复杂查询，采用连贯操作方式
 * ;mysql驱动
 * 使用示例:
 * SQLBuilder(User.class).fields("name","sex","dept_name").join(Dept.class,"User.did=Dept.id").where(HashMap).order("name","desc").page(1,10).buildSql();
 * 说明：
 * 		where的用法：
 * 			Map<String,Object> map = new HashMap<String,Object>();
 * 			map.put("name","Simon"); //变成name='Simon'
 * 			map.put("name",new String[]{"like","%Sim%"});
 * 		where(map,"AND");
 * 		where(map,"OR"); //where可以多次使用
 * @author Simon
 */
public class SQLBuilder{
	
	private String pojoName;
	private String tableName;
	private String fieldString;
	private String whereString;
	private String pageString;
	private String orderString;
	private String joinString;
	private String groupString;
	private List<String> tableFields = new ArrayList<String>();
	private String lastSql;

	private static Logger logger = Logger.getLogger(SQLBuilder.class);
	
	private SQLBuilder(){}
	
	private SQLBuilder(Class<?> poClass){
		setTableName(parseTableName(poClass));
		setPojoName(poClass.getSimpleName());
		//将类的属性变成字段名，方式是驼峰转下划线
		Field[] fieldLists = poClass.getDeclaredFields();
		for (Field field : fieldLists) {
			tableFields.add(Fn.underscoreName(field.getName()));
		}
	}
	
	public static SQLBuilder getSQLBuilder(Class<?> poClass){
		return new SQLBuilder(poClass);
	}
	
	/**
	 * 设置查询字段,需写在连贯操作的第一位<br/>
	 * 支持字符串形式fields("id,user_name") -> "select id,user_name AS userName" <br/>
	 * 支持数组参数fields("id","user_name") -> 效果跟上一句一样<br/>
	 * 说明:1.下划线命名的字段返回时会变成驼峰法，跟类的属性名一致<br/>
	 * 	   2.包含函数的字段，请自行AS，设置别名
	 * @param fields
	 * @return
	 */
	public SQLBuilder fields(String... fieldsArr){
		
		String fs = "";
		if(fieldsArr.length==0 || fieldsArr[0].equals("*")){
			fieldsArr = (String[])tableFields.toArray(new String[tableFields.size()]);
		}
		if(fieldsArr.length==1){ //fields("id,name")单个参数形式
			fieldsArr = fieldsArr[0].split(","); //把字符串分割成数组
		}
		for(String f:fieldsArr){
			String fLower = f.toLowerCase();
			//若不是应用函数、不包含AS，则处理把字段名处理成驼峰名
			if(!(fLower.contains("(") ||  fLower.contains(")") || fLower.contains(" as "))){
				fs += f + " AS " + Fn.camelName(f) + ",";
			}else{
				fs += f + ",";
			}
		}
		fs = fs.substring(0, fs.length()-1); //删除最后的逗号
		setFieldString(fs);
		return this;
	}
	
	/**
	 * 设置where条件,where可以多次使用
	 * @param where 条件组装成map,可以用pojo做example再转成map
	 * @param logicType 条件结合方式,可选值AND或OR
	 * @return
	 */
	public SQLBuilder where(Map<String,Object> where,String... logicType){
		if(where != null && !where.isEmpty()){
			if(logicType.length==0){
				logicType = new String[]{"AND"};
			}
			if(getWhereString()==null){
				setWhereString("WHERE " + parseWhereMap(where,logicType[0]));
			}else{
				setWhereString(getWhereString() + logicType + " " + parseWhereMap(where,logicType[0]));
			}
		}
		return this;
	}
	
	/**
	 * 设置where条件
	 * @param where sql字符串字面量,如"user=simon"
	 * @return
	 */
	public SQLBuilder where(String where,String... logicType){
		if(where != null && !where.trim().isEmpty()){
			if(logicType.length==0){
				logicType = new String[]{"AND"};
			}
			where = where.trim();
			if(getWhereString()==null){
				setWhereString("WHERE " + where + " ");
			}else{
				setWhereString(getWhereString() + logicType[0] + " " + where + " ");
			}
		}
		return this;
	}
	
	/**
	 * 设置group by
	 * @param field 多个字段用逗号隔开
	 * @return
	 */
	public SQLBuilder group(String field){
		setGroupString("GROUP BY " + field+" ");
		return this;
	}
	
	/**
	 * 设置排序
	 * @param orderField 排序字段
	 * @param orderType 排序类型
	 * @return
	 */
	public SQLBuilder order(String orderField, String orderType){
		orderType = orderType.toUpperCase();
		if(!(orderType.equals("ASC") || orderType.equals("DESC"))){
			orderType = "ASC";
		}
		setOrderString("ORDER BY " + orderField + " " +orderType + " ");
		return this;
	}
	
	/**
	 * 设置分页
	 * @param page 当前页码
	 * @param rows 每页显示数量
	 * @return
	 */
	public SQLBuilder page(Integer page,Integer rows){
		if(page!=null && rows!=null){
			setPageString("LIMIT " + (page-1)*rows + "," + rows + " ");
		}
		return this;
	}
	
	
	/**
	 * 解析jqui的分页和排序参数
	 * @param pageMap
	 */
	public SQLBuilder parseUIPageAndOrder(Map<String,String> pageMap){
		//page,rows,sort,order是前段控件传过来的固定参数
		if(pageMap.get("page")!=null && pageMap.get("rows")!=null){
			int page = Integer.parseInt(pageMap.get("page"));
			int rows = Integer.parseInt(pageMap.get("rows").toString());
			setPageString("LIMIT " + (page-1)*rows + "," + rows + " ");
		}
		if(pageMap.get("sort")!=null && pageMap.get("order")!=null){
			String orderField = pageMap.get("sort").toString();
			String orderType = pageMap.get("order").toString().toUpperCase();
			setOrderString("ORDER BY " + orderField + " " +orderType + " ");
		}
		return this;
	}
	
	/**
	 * 设置多表连接
	 * @param anotherPoName 需要连接表对应的pojo名称
	 * @param joinFieldString 连接条件
	 * @return
	 */
	public SQLBuilder join(Class<?> anotherPoClass,String joinFieldString){
		String sql = "JOIN " + parseTableName(anotherPoClass) + " AS " + anotherPoClass.getSimpleName() +" ON " + joinFieldString + " ";
		setJoinString(nullToBlank(getJoinString()) + sql); //拼接多个join
		return this;
	}
	
	/**
	 * 设置多表连接
	 * @param anotherPoName 需要连接表对应的pojo名称
	 * @param joinFieldString 连接条件
	 * @param joinType 连接类型,可选值LEFT,RIGHT,FULL
	 * @return
	 */
	public SQLBuilder join(Class<?> anotherPoClass,String joinFieldString,String joinType){
		joinType = joinType.toUpperCase();
		if(!(joinType.equals("LEFT") || joinType.equals("RIGHT") || joinType.equals("FULL"))){
			joinType = "";
		}else{
			joinType +=  " ";
		}
		String sql = joinType + "JOIN " + parseTableName(anotherPoClass) + " AS " + anotherPoClass.getSimpleName() +" ON " + joinFieldString + " ";
		setJoinString(nullToBlank(getJoinString()) + sql); //拼接多个join
		return this;
	}
	
	/**
	 * 组装select的sql语句
	 * @return
	 */
	public String buildSql(){
		if(getFieldString()==null){
			throw new NullPointerException("Must invoke method field first!");
		}
		
		String sql = "SELECT ";
			   sql+= getFieldString() + " ";
			   sql+= "FROM " + getTableName() + " AS " + getPojoName() + " ";
			   sql+= nullToBlank(getJoinString());
			   sql+= nullToBlank(getWhereString());
			   sql+= nullToBlank(getGroupString());
			   sql+= nullToBlank(getOrderString());
			   sql+= nullToBlank(getPageString());
		//重置查询条件
	    resetQuery();
	    this.setLastSql(sql);  //记录最后一条sql语句
	    logger.info(sql);
	    return sql;
	}
	
	
	/**
	 * 解析查询条件
	 * @param whereMap 要解析的HashMap
	 * @param logicType 条件结合类型,AND或OR
	 * @return 条件字符串: key1 = value1 AND key
	 */
	public String parseWhereMap(Map<String, Object> whereMap,String logicType){
		logicType = logicType.toLowerCase();
		if(!(logicType.equals("and") || logicType.equals("or"))){
			logicType = "and";
		}
		String whereString = "";
		Iterator<String> keys = whereMap.keySet().iterator();	
		while(keys.hasNext()){
			String k = keys.next();
			Object v = whereMap.get(k);
			if(k.equals("_string")){ //允许混合直接字符串查询
				whereString += v.toString() + " " + logicType + " ";
			}else{
				if(v.getClass().isArray() && ((String[])v).length == 2){ //whereMap.put("name",new String[]{"like","%Simon%"})
					whereString += k + " " + ((String[])v)[0] + " \'" + ((String[])v)[1] + "\' " + logicType + " ";
				}else{
					whereString += k + "=\'" + v.toString() + "\' " + logicType + " ";
				}
			}
		}
		whereString = whereString.trim();
		whereString = Fn.rtrim(whereString,logicType);
		return whereString;
	}
	
	/**
	 * 解析表名,实体类有table注解则读取注解，没有则按“配置的前缀+类名驼峰转下划线”来解析
	 * @throws IOException 
	 */
	public String parseTableName(Class<?> poClass){
		Table table = poClass.getAnnotation(Table.class);
		String tableName = "";
		if(table==null){ //实体类不存在table注解则自动按规则解析表名
			InputStream in = SQLBuilder.class.getResourceAsStream("/jdbc.properties");
			Properties p = new Properties();
			try {
				p.load(in);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tableName = Fn.nullToEmpty(p.getProperty("jdbc.prefix"))+Fn.underscoreName(poClass.getSimpleName());
		}else{
			tableName = table.name(); //实体类直接读取table注解
		}
		return tableName;
	}
	
	/**
	 * 重置查询条件
	 */
	public void resetQuery(){
		setFieldString(null);
		setWhereString(null);
		setOrderString(null);
		setPageString(null);
		setJoinString(null);
	}
	
	/**
	 * 把null变成""
	 * @param str
	 * @return
	 */
	private String nullToBlank(String str){
		if(str==null){
			str = "";
		}
		return str;
	}
	

	/**
	 * @return the pojoName
	 */
	public String getPojoName() {
		return pojoName;
	}

	/**
	 * @param pojoName the pojoName to set
	 */
	public void setPojoName(String pojoName) {
		this.pojoName = pojoName;
	}

	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * @return the fieldString
	 */
	public String getFieldString() {
		return fieldString;
	}

	/**
	 * @param fieldString the fieldString to set
	 */
	public void setFieldString(String fieldString) {
		this.fieldString = fieldString;
	}

	/**
	 * @return the whereString
	 */
	public String getWhereString() {
		return whereString;
	}

	/**
	 * @param whereString the whereString to set
	 */
	public void setWhereString(String whereString) {
		this.whereString = whereString;
	}

	/**
	 * @return the pageString
	 */
	public String getPageString() {
		return pageString;
	}

	/**
	 * @param pageString the pageString to set
	 */
	public void setPageString(String pageString) {
		this.pageString = pageString;
	}

	/**
	 * @return the orderString
	 */
	public String getOrderString() {
		return orderString;
	}

	/**
	 * @param orderString the orderString to set
	 */
	public void setOrderString(String orderString) {
		this.orderString = orderString;
	}

	/**
	 * @return the joinString
	 */
	public String getJoinString() {
		return joinString;
	}

	/**
	 * @param joinString the joinString to set
	 */
	public void setJoinString(String joinString) {
		this.joinString = joinString;
	}

	/**
	 * @return the lastSql
	 */
	public String getLastSql() {
		return lastSql;
	}

	/**
	 * @param lastSql the lastSql to set
	 */
	public void setLastSql(String lastSql) {
		this.lastSql = lastSql;
	}

	/**
	 * @return the groupString
	 */
	public String getGroupString() {
		return groupString;
	}

	/**
	 * @param groupString the groupString to set
	 */
	public void setGroupString(String groupString) {
		this.groupString = groupString;
	}

	/**
	 * @return the tableFields
	 */
	public List<String> getTableFields() {
		return tableFields;
	}

	/**
	 * @param tableFields the tableFields to set
	 */
	public void setTableFields(List<String> tableFields) {
		this.tableFields = tableFields;
	}

}
