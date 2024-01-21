package at.fhv.master.laendleenergy.datacollector.model.usertypes;

import at.fhv.master.laendleenergy.datacollector.model.DeviceCategory;
import at.fhv.master.laendleenergy.datacollector.model.Tag;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TagUserType extends ListArrayType {

    @Override
    public Object nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner) throws SQLException {
        String deviceId = rs.getString("device_id");
        String json = rs.getString(position);
        if(json != null){
            json = json.replaceAll("(?<=\\{)\\\"|\\\"(?=})|(\\\\)|(?<=\\))\\\"|\\\"(?=\\()", "");
            List<Tag> tags = parseTupleString(json, deviceId);
            return tags;

        }
        return new ArrayList<>();
    }

    private static List<Tag> parseTupleString(String input, String deviceId) {
        List<Tag> tupleList = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\(([^,]+),\"([^\"]+)\",\"\\[\"\"([^\"]+?)\"\",\"\"([^\"]+?)\"\"\\]\"\\)");
        Matcher matcher = pattern.matcher(input);
        while(matcher.find()) {
            String deviceCategoryId = matcher.group(1);
            String tagDescription = matcher.group(2);
            LocalDateTime timeStart = LocalDateTime.parse(matcher.group(3), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDateTime timeEnd = LocalDateTime.parse(matcher.group(4), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            DeviceCategory deviceCategory = new DeviceCategory(deviceCategoryId);
            Tag tag = new Tag(tagDescription, deviceCategory, timeStart, timeEnd, deviceId,null);
            tupleList.add(tag);
        }

        return tupleList;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object tags, int index, SharedSessionContractImplementor session) throws SQLException {
        List<Tag> tagsList = (List<Tag>) tags;
        if(tagsList.size() == 0){
            st.setNull(index, Types.ARRAY);
        }
        else{
            List<String> arr = new ArrayList<>();
            for (int i = 0; i < tagsList.size(); i++) {
                Tag tag = tagsList.get(i);
                String deviceCategoryName = tag.getDeviceCategoryName().contains(" ")
                        ? "\'" + tag.getDeviceCategoryName() + "\'" : tag.getDeviceCategoryName();
                String tagName = tag.getDeviceName().contains(" ")
                        ? "\'" + tag.getDeviceName() + "\'" : tag.getDeviceName();
                String dateRange = "[\"\"" + tag.getStarTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        + "\"\"" + "," + tag.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\"\"]";
                arr.add("(" + deviceCategoryName + "," + tagName + "," + dateRange + ")");
            }
            st.setArray(index, session.getJdbcConnectionAccess().obtainConnection().createArrayOf("TAG_RECORD", arr.toArray()));
        }
    }

}
