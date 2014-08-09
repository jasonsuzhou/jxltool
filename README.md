JXLTool

---
- Summary
```
This is a maven project.
This tool used to import and export 2003 excel more easily.
Based on configuration, this tool is very flexible。
This tool depends on below jar file(you can also find inside pom.xml):
	- dom4j.jar                          version : 1.6.1
	- jxl.jar                            version : 2.6.12
	- org.apache.commons.lang3.jar       version : 3.1
```
- Export sample code
```
	@Test
	public void generateExcel() {
		List<User> list = youMayGetFromDB();
		try {
			File configFile = new File("./src/test/resources/exportConfig-test.xml");
			ExportExcelConfig.initConfig(configFile);
			File outputFile = new File("/Users/jasonyao/git/jxltool/test.xls");
			ExcelWriteUtil<User> writeUtil = new JXLWriteUtil<User>(new FileOutputStream(outputFile));
			writeUtil.generateExcel(list, ExportExcelConfig.getExportBean("demo"), "testExport");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
```
- Import sample code
```
@Test
	public void getExcelData() {
		String configFilePath = "./src/test/resources/importConfig-test.xml";
		String testImportFilePath = "/Users/jasonyao/git/jxltool/test.xls";
		try {
			ImportExcelConfig.initConfig(new File(configFilePath));
			InputStream inputStream = new FileInputStream(new File(testImportFilePath));
			ExcelReadUtil util = new JXLReadUtil(inputStream, ImportExcelConfig.getImportBean("demo"));
			String validationResult = util.getValidationResult();
			if (StringUtils.isNotBlank(validationResult)) {
				// objs contains all the data inside excel
				List<Object[]> objs = util.getFirstSheetRowValues();
			} else {
				System.out.println(validationResult);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
```
- The export configure file show like below:
```
<?xml version="1.0" encoding="UTF-8"?>
<export-config>
	<!-- class: the bean class of your APP. totalCols:the total fields which will be exported. -->
	<bean id="demo" class="User" totalCols="4">
		<titles ref="standardTitle">
			<title seq="0" name="User Name"></title>
			<title seq="1" name="Age"></title>
			<title seq="2" name="Birthday"></title>
			<title seq="3" name="Balance"></title>
		</titles>
		<fields>
			<field seq="0" getterMethod="getUsername">
				<format ref="standardText"></format>
			</field>
			<field seq="1" getterMethod="getAge">
				<format ref="standardText"></format>
			</field>
			<field seq="2" getterMethod="getBirthday">
				<format ref="standardDate"></format>
			</field>
			<field seq="3" getterMethod="getBalance">
				<format ref="standardNumber">
					<!--if do not define the 'ref' value, you can define the style here -->
				</format>
			</field>
		</fields>
	</bean>
	<formats>
		<format id="standardTitle">
			<width>15</width>
			<height>20</height>
			<align>CENTRE</align>
			<vertical>CENTRE</vertical>
			<fontSize>9</fontSize>
			<blod>true</blod>
			<wrap>true</wrap>
		</format>
		<format id="standardText">
			<align>LEFT</align>
			<vertical>CENTRE</vertical>
			<fontSize>8</fontSize>
			<blod>false</blod>
			<wrap>true</wrap>
			<backColor>WHITE</backColor>
		</format>
		<format id="standardFixLength">
			<align>CENTRE</align>
			<vertical>CENTRE</vertical>
			<fontSize>8</fontSize>
			<blod>false</blod>
			<wrap>false</wrap>
			<backColor>WHITE</backColor>
		</format>
		<format id="standardDate">
			<width>600</width>
			<height>20</height>
			<align>CENTRE</align>
			<vertical>CENTRE</vertical>
			<fontSize>8</fontSize>
			<blod>false</blod>
			<wrap>false</wrap>
			<backColor>WHITE</backColor>
			<dateFormat>dd/MM/yyyy</dateFormat>
		</format>
		<format id="standardNumber">
			<align>RIGHT</align>
			<vertical>CENTRE</vertical>
			<fontSize>8</fontSize>
			<blod>false</blod>
			<wrap>false</wrap>
			<backColor>WHITE</backColor>
			<dataFormat>#,##0.00</dataFormat>
		</format>
	</formats>
</export-config>
```
- The import configure file show like below:
```
<?xml version="1.0" encoding="UTF-8"?>
<import-config>
	<!-- startRow starts with 0, totalCols is how many columns will be parsed -->
	<bean id="demo" startRow="1" totalCols="4">
		<field seq="0" name="User Name">
			<rule id="1" desc="shall be mandatory field"></rule>
			<rule id="2" desc="max length check">20</rule>
		</field>
		<field seq="1" name="Age">
			<rule id="1"></rule>
			<rule id="2">2</rule>
		</field>
		<field seq="2" name="Birthday">
			<rule id="4" desc="shall be date column">dd/MM/yyyy</rule>
		</field>
		<field seq="3" name="Balance">
			<rule id="3" desc="shall be amount column">#,##0.00</rule>
		</field>
	</bean>
</import-config>
```
- How to share local project to GitHub
```
goto the project folder
	git init
	git remote add origin https://github.com/jasonsuzhou/jxltool.git
add all files
	git add .
local commit
	git commit -a -m "Initial project"
do the real commit
	git push -u origin --all
```