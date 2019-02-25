# stringcode
如何利用getBytes 和 new String() 来进行编码转换呢？  网上流传着一种错误的方法:GBK--> UTF-8:    new String( s.getBytes("GBK") , "UTF-8);   ,这种方式是完全错误的，因为getBytes 的编码与  UTF-8 不一致，肯定是乱码。

## 参考资料
* [Java 正确的做字符串编码转换](https://blog.csdn.net/H12KJGJ/article/details/73496528)

```text
Java 正确的做字符串编码转换
字符串的内部表示？
字符串在java中统一用unicode表示( 即utf-16 LE) , 
对于 String s = "你好哦!";
如果源码文件是GBK编码, 操作系统（windows）默认的环境编码为GBK，那么编译时,  JVM将 按照GBK编码将字节数组解析成字符，然后将字符转换为unicode格式的字节数组，作为内部存储。
当打印这个字符串时，JVM 根据操作系统本地的语言环境，将unicode转换为GBK，然后操作系统将GBK格式的内容显示出来。
 
当源码文件是UTF-8, 我们需要通知编译器源码的格式，javac -encoding utf-8 ... , 编译时，JVM按照utf-8 解析成字符，然后转换为unicode格式的字节数组， 那么不论源码文件是什么格式，同样的字符串，最后得到的unicode字节数组是完全一致的，显示的时候，也是转成GBK来显示（跟OS环境有关）
 
乱码如何产生？ 本质上都是由于 字符串原本的编码格式 与 读取时解析用的编码格式不一致导致的。
例如：
String s = "你好哦!";
System.out.println( new String(s.getBytes(),"UTF-8")); //错误，因为getBytes()默认使用GBK编码， 而解析时使用UTF-8编码，肯定出错。
其中 getBytes() 是将unicode 转换为操作系统默认的格式的字节数组，即"你好哦"的 GBK格式，
new String (bytes, Charset) 中的charset 是指定读取 bytes 的方式，这里指定为UTF-8,即把bytes的内容当做UTF-8 格式对待。
如下两种方式都会有正确的结果，因为他们的源内容编码和解析用的编码是一致的。
System.out.println( new String(s.getBytes(),"GBK"));
System.out.println( new String(s.getBytes("UTF-8"),"UTF-8"));
 
那么，如何利用getBytes 和 new String() 来进行编码转换呢？  网上流传着一种错误的方法:
GBK--> UTF-8:    new String( s.getBytes("GBK") , "UTF-8);   ,这种方式是完全错误的，因为getBytes 的编码与  UTF-8 不一致，肯定是乱码。
但是为什么在tomcat 下，使用 new String(s.getBytes("iso-8859-1") ,"GBK") 却可以用呢？ 答案是：
tomcat 默认使用iso-8859-1编码， 也就是说，如果原本字符串是GBK的，tomcat传输过程中，将GBK转成iso-8859-1了，
默认情况下，使用iso-8859-1读取中文肯定是有问题的，那么我们需要将iso-8859-1 再转成GBK， 而iso-8859-1 是单字节编码的，
即他认为一个字节是一个字符， 那么这种转换不会对原来的字节数组做任何改变，因为字节数组本来就是由单个字节组成的，
如果之前用GBK编码，那么转成iso-8859-1后编码内容完全没变， 则 s.getBytes("iso-8859-1")  实际上还是原来GBK的编码内容
则 new String(s.getBytes("iso-8859-1") ,"GBK")  就可以正确解码了。 所以说这是一种巧合。
 
如何正确的将GBK转UTF-8 ? （实际上是unicode转UTF-8)
String gbkStr = "你好哦!"; //源码文件是GBK格式，或者这个字符串是从GBK文件中读取出来的, 转换为string 变成unicode格式
//利用getBytes将unicode字符串转成UTF-8格式的字节数组
byte[] utf8Bytes = gbkStr.getBytes("UTF-8"); 
//然后用utf-8 对这个字节数组解码成新的字符串
String utf8Str = new String(utf8Bytes, "UTF-8");
简化后就是:
unicodeToUtf8 (String s) {
return new String( s.getBytes("utf-8") , "utf-8");
}
UTF-8 转GBK原理也是一样
return new String( s.getBytes("GBK") , "GBK");
 
 其实核心工作都由  getBytes(charset) 做了。
getBytes 的JDK 描述：Encodes this String into a sequence of bytes using the named charset, storing the result into a new byte array.
 
另外对于读写文件，
OutputStreamWriter w1 = new OutputStreamWriter(new FileOutputStream("D:\\file1.txt"),"UTF-8");
InputStreamReader( stream, charset)
可以帮助我们轻松的按照指定编码读写文件
```