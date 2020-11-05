一款纯粹的Java序列化框架
====================

### 特性
- **纯粹**, 严格按照字节顺序和长度写入，不产生额外的描述性信息；
- **性能**, 基于Netty的ByteBuf，可使用池化内存与堆外内存提升性能；
- **多版本**, 同一个Class支持多个版本的配置。 

### 场景
- 适用于多数序列化场景，用于传输或存储对象；
- 开发初期的目的是为了解析部标、国标相关的通讯协议。

### 使用
```java

public class Test {

    public static void main(String[] args) {
        //开启序列化过程分析
        FieldFactory.EXPLAIN = true;

        //获得多个版本的协议定义
        Map<Integer, Schema<Foo>> multiVersionSchema = ProtostarUtil.getSchema(Foo.class);
        //默认的版本是0
        Schema<Foo> schema = multiVersionSchema.get(0);
        //使用netty的Unpooled申请初始32字节的空间
        ByteBuf buffer = Unpooled.buffer(32);
        //将foo写入到缓冲区
        schema.writeTo(buffer, foo());
        String hex = ByteBufUtil.hexDump(buffer);
        System.out.println(hex);

        //将缓冲区的字节解析为对象
        Foo foo = schema.readFrom(buffer);
        System.out.println(foo);
    }

    public static Foo foo() {
        Foo foo = new Foo();
        foo.setName("张三");
        foo.setId(128);
        foo.setDateTime(LocalDateTime.of(2020, 7, 7, 19, 23, 59));
        return foo;
    }

    public static class Foo {

        private String name;
        private int id;
        private LocalDateTime dateTime;

        @Field(index = 0, type = DataType.STRING, lengthSize = 1, desc = "名称")
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Field(index = 1, type = DataType.WORD, desc = "ID")
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @Field(index = 3, type = DataType.BCD8421, desc = "日期")
        public LocalDateTime getDateTime() {
            return dateTime;
        }

        public void setDateTime(LocalDateTime dateTime) {
            this.dateTime = dateTime;
        }
    }
}
```

### Maven
  ```xml
  <dependency>
    <groupId>io.github.yezhihao</groupId>
    <artifactId>protostar</artifactId>
    <version>1.0.0.RELEASE</version>
  </dependency>
  ```

更多的例子请参考Test目录

项目会不定期进行更新，建议star和watch一份，您的支持是我最大的动力。

如有任何疑问或者BUG，请联系我，非常感谢。

技术交流QQ群：[906230542]
