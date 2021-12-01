# excel_parser
A tool for parsing xls or xlsx files  
## 方法  
方法提供了2个service用来解析excel文件：  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; XLSService 的 parseXls 方法用来解析 xls 文件  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; XLSXService 的 parseXlsx 方法用来解析 xlsx 文件  
使用方式可参考 Main 方法  

## 配置文件 config.properties  
data_dir 为excel数据文件所在目录  
result_dir 为解析结果存放目录，可以不配置，若不配置则结果文件存放在data_dir指定的目录下  
recursive 表示是否递归解析data_dir目录下的excel文件，若配置成false, 则只会解析data_dir目录下的excel文件而不处理其子目录下的文件  
buffer_size 用来指定处理数据的缓冲行数，若excel文件列数特别多，每一列的值特别大，则可以适量减少buffer_size的值




