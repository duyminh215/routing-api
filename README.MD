Code project dùng để tính khoảng cách giữa 2 điểm latitude, longitude theo đường đi bộ hoặc xe máy, 
car.

Trong project có file resources/vietnam-latest.osm.pbf đã được chia thành các file nhỏ vì github ko cho phép file quá lớn.

Các file nhỏ này từ 00 -> 02.

Trước khi chạy code, merge các file này thành 1 file duy nhất resources/vietnam-latest.osm.pbf

Sau khi merge file "resources/vietnam-latest.osm.pbf", chạy file main để thử kết quả.

#### **1.Cách lấy latitude và longitude bằng google map:**

Latitude (vĩ độ), longitude (kinh độ) là hai tọa độ quan trọng trong việc xác định vị trí 1 điểm trên bản đồ.

Google map đã hỗ trợ lấy latitude và longitude của một điểm bất kì trên thế giới.

Để lấy được kinh độ và vĩ độ của một địa điểm bất kì trên google map, các bạn chỉ cần ấn chuột phải vào điểm đó và ấn vào dòng \"Đây là gì?\".

#### **2. Tính khoảng cách đường thẳng (đường chim bay) giữa 2 điểm trên bản đồ:**
Giả sử ta có 2 điểm, một điểm P1 (la1, lo1) và điểm P2(la2, lo2).

Vấn đề bây giờ là tính khoảng cách nối thẳng giữa 2 điểm trên như thế nào?

Chúng ta có thể search trên mạng để ra được công thức của việc tính toán khoảng cách giữa 2 điểm dựa vào latitude vào longitude. 

Công thức đó các bạn có thể vào đây tham khảo: http://www.movable-type.co.uk/scripts/latlong.html.

Với công thức trên, chúng ta có thể sử dụng code để tính toán được khoảng cách đường thẳng giữa 2 điểm bất kì dựa vào latitude và longitude. 

Code java của hàm đó sẽ như sau:
```
public static double distanceBetween2Points(double la1, double lo1, double la2, double lo2) {
    double R = 6371;
    double dLat = (la2 - la1) * (Math.PI / 180);
    double dLon = (lo2 - lo1) * (Math.PI / 180);
    double la1ToRad = la1 * (Math.PI / 180);
    double la2ToRad = la2 * (Math.PI / 180);
    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(la1ToRad)
                * Math.cos(la2ToRad) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    double d = R * c;
    return d;
}
```

Trên đây là hàm tính khoảng cách đường thẳng nối giữa 2 điểm có tọa độ latitude và longitude trên bản đồ.

Trên thực tế, khoảng cách mà chúng ta di chuyển không phải là một đường thẳng nối giữa 2 điểm đó mà chúng ta phải đi theo các con đường đi giữa 2 điểm đó.

Do đó, khoảng cách thực tế mà ta đi có thể sẽ khác rất nhiều và xa hơn rất nhiều với con số mà chúng ta tính toán được ở hàm trên.

Yêu cầu đặt ra bây giờ là chúng ta cần phải tính toán khoảng cách đường đi giữa 2 điểm đó. 

Google đã có hàm đó cho chúng ta. Tuy nhiên, muốn sử dụng hàm đó với google map thì chúng ta cần phải trả phí.

Qua nghiên cứu thì mình đã tìm được một thư viện open source rất hay để tính khoảng cách đường đi giữa 2 tọa độ.

####3. Tính khoảng cách đường đi giữa 2 điểm có tọa độ latitude và longitude biết trước:#####
Thư viện mình đã sử dụng có tên là Graphhopper, mã nguồn mở ở link sau: https://github.com/graphhopper/graphhopper.

Các bạn cần phải có một data về bản đồ Việt Nam import vào trong thư viện này để tính toán khoảng cách giữa 2 điểm có latitude và longitude biết trước.

Các bạn download file về tọa độ bản đồ Việt Nam ở đây http://download.geofabrik.de/asia/vietnam.html và sẽ được một file có dạng vietnam.osm.pbf.

Đây là file chứa tọa độ về các con đường mà những người xây dựng bản đồ đã dựng sẵn. 

Dựa vào các tọa độ này, chúng ta sẽ tính được khoảng cách đường đi giữa 2 điểm bất kì.

Để có thể tính toán khoảng cách thực tế giữa 2 điểm bất kì, chúng ta sẽ load thư viện graphoper về. 

Sau đó sử dụng hàm tính khoảng cách đường đi giữa 2 điểm. 

Vì là tính khoảng cách đường đi giữa 2 điểm nên chúng ta cần biết sử dụng loại phương tiện nào. 

Qua quá trình tìm hiểu của mình, các bạn nên chọn phương tiện là \"car\" sẽ ra được khoảng cách đường đi sát với google nhất.

Sau đây là source code mình đã test để tính khoảng cách.