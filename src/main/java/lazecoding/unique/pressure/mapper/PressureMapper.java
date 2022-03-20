package lazecoding.unique.pressure.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PressureMapper {

    void pressureUuid(@Param("uid") String uid,@Param("uname") String uname,@Param("soft") Long soft);

    void pressureLong(@Param("uid") Long uid,@Param("uname") String uname,@Param("soft") Long soft);

}
