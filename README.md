# CardBannerDemo
## 效果图
![image](https://github.com/xuezj/DragChooseDemo/blob/master/demo.gif)

## Attributes属性（布局文件中的自定义属性）

|     变量名    |  类型  |  说明   |
| :-------------: |:-------------:| :-----:|
| main_title_text_color | color | 主标题文字颜色(默认白色) |
| subtitle_title_text_color | color  |   副标题（不设置默认为白色） |
| divider_width | dimension |    卡片之间的间隔 |
| main_title_text_size | dimension |    主标题字体大小 |
| subtitle_title_text_size | dimension |  副标题字体大小 |
| border_width | dimension |  左右卡片展示的宽度 |


## 方法
|     方法名    |  说明   |
| :-------------:| :-----:|
| setDatas | 使用原生卡片设置数据（必须使用lib中的ImageData新建集合） |
| setDataCount | （使用自定义item）设置数据集合大小（配合setBannerAdapter使用） |
| setBannerAdapter | （使用自定义item）  设置自定义item的Adapter（配合setDataCount使用） |
| setDelayTime | 设置自动轮播时间间隔（单位毫秒，默认为2000） |

## 类
ImageData.class
原生item的数据集合实体类

BannerViewHolder.class
自定义item实现ViewHolder的被继承类
## 使用
###添加依赖
Gradle
```
dependencies{
    compile 'com.xuezj.cardbanner:cardbanner:1.0.0'
}
```
###工程中使用
布局文件中的使用
```xml
<com.xuezj.cardbanner.CardBanner
            android:id="@+id/banner"
            android:layout_width="match_parent"
            android:layout_height="180dp"
            app:divider_width="10dp"
            app:main_title_text_color="#fff"
            app:subtitle_title_text_color="#fff"
            app:main_title_text_size="15dp"
            app:subtitle_title_text_size="12dp"
            app:border_width="30dp"/>
 
```
代码中调用
1.原生
```Java
        List<ImageData> imageData = new ArrayList<>();
        ImageData b1 = new ImageData();
        b1.setImage("http://ww1.sinaimg.cn/large/610dc034ly1fhyeyv5qwkj20u00u0q56.jpg");
        b1.setMainTitle("第一张图片");
        imageData.add(b1);

        ImageData b2 = new ImageData();
        b2.setImage("https://ws1.sinaimg.cn/large/610dc034gy1fhvf13o2eoj20u011hjx6.jpg");
        b2.setSubtitleTitle("23-7期");
        imageData.add(b2);
        ImageData b3 = new ImageData();
        b3.setImage("http://ww1.sinaimg.cn/large/610dc034ly1fhxe0hfzr0j20u011in1q.jpg");
        imageData.add(b3);
        //2.然后调用setDatas方法填充数据，再start()就可以了，
        // 是否自动轮播setPlay可以不设置，默认为自动轮播即为ture
        cardBanner.setDatas(imageData).setPlay(true).start();
        cardBanner.setOnItemClickListener(new CardBanner.OnItemClickListener() {
            @Override
            public void onItem(int position) {
                Toast.makeText(MainActivity.this, "position:" + position, Toast.LENGTH_SHORT).show();
            }
        });
```
2.自定义item
```Java
        final List<String> image=new ArrayList<>();
        image.add("http://ww1.sinaimg.cn/large/610dc034ly1fhyeyv5qwkj20u00u0q56.jpg");
        image.add("https://ws1.sinaimg.cn/large/610dc034gy1fhvf13o2eoj20u011hjx6.jpg");
        image.add("http://ww1.sinaimg.cn/large/610dc034ly1fhxe0hfzr0j20u011in1q.jpg");
        cardBanner2.setDataCount(imageData.size()).setBannerAdapter(new BannerAdapter() {
            @Override
            public BannerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                ViewHolder h = new ViewHolder(LayoutInflater.from(MainActivity.this)
                        .inflate(R.layout.banner_item_demo, parent, false));
                return h;
            }

            @Override
            public void onBindViewHolder(BannerViewHolder holder, int position) {
                ViewHolder VH = (ViewHolder) holder;
                Glide.with(MainActivity.this)
                        .load(image.get(position)).centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .bitmapTransform(new CropCircleTransformation(getContext()))
                        .into(VH.roundedImageView);
            }
        });


        cardBanner2.start();
        cardBanner2.setOnItemClickListener(new CardBanner.OnItemClickListener() {
            @Override
            public void onItem(int position) {
                Toast.makeText(MainActivity.this, "position:" + position, Toast.LENGTH_SHORT).show();
            }
        });

```