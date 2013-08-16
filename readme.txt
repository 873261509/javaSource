工程简介：
本工程主要用于和华道以及pharos通讯使用的中间Webservices工程。

1、给华道通讯：必须作为一个ws服务器端，提供方法给华道，用来保存理赔数据
2、给pharos通讯：I、如果使用pharos的缓存，可以通过调用pharos的远程接口来进行数据保存。
			II、如果使用ws和pharos通讯，则需要作为客户端调用pharos的ws接口