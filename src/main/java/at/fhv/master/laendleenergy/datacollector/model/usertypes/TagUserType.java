package at.fhv.master.laendleenergy.datacollector.model.usertypes;

import at.fhv.master.laendleenergy.datacollector.model.DeviceCategory;
import at.fhv.master.laendleenergy.datacollector.model.Tag;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TagUserType extends ListArrayType {

    @Override
    public Object nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner) throws SQLException {
        String json = rs.getString(position);
        if(json != null){
            System.out.println();
            json = json.replaceAll("(?<=\\{)\\\"|\\\"(?=})|(\\\\)|(?<=\\))\\\"|\\\"(?=\\()", "");
            List<Tag> tags = parseTupleString(json);
            return tags;

        }
        return new ArrayList<>();
    }

    private static List<Tag> parseTupleString(String input) {
        List<Tag> tupleList = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\(([^,]+),\"([^\"]+)\"\\)");
        Matcher matcher = pattern.matcher(input);
        while(matcher.find()) {
            String deviceCategoryId = matcher.group(1);
            String tagDescription = matcher.group(2);
            DeviceCategory deviceCategory = new DeviceCategory(deviceCategoryId, null);
            Tag tag = new Tag(tagDescription, deviceCategory, null);
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
                StringBuffer jsonBuffer = new StringBuffer();
                Tag tag = tagsList.get(i);
                arr.add("(" + tag.getDeviceCategoryName() + ", \'" + tag.getName() + "\')");
                //if(i < tagsList.size() - 1)
                   // jsonBuffer.append(",");
            }
            st.setArray(index, session.getJdbcConnectionAccess().obtainConnection().createArrayOf("TAG_RECORD", arr.toArray()));

            //array[('washing_machine', 'Arschloch')]::TAG_RECORD[]
            //st.setObject(index, jsonBuffer.toString(), SqlTypes.OTHER);
        }
    }

}
