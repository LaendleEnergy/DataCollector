package at.fhv.master.laendleenergy.datacollector.model.usertypes;

import at.fhv.master.laendleenergy.datacollector.model.Tag;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TagUserType implements UserType<Tag> {

    @Override
    public int getSqlType() {
        return 0;
    }

    @Override
    public Class<Tag> returnedClass() {
        return Tag.class;
    }

    @Override
    public boolean equals(Tag x, Tag y) {
        return false;
    }

    @Override
    public int hashCode(Tag x) {
        return 0;
    }

    @Override
    public Tag nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner) throws SQLException {
        return null;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Tag value, int index, SharedSessionContractImplementor session) throws SQLException {

    }

    @Override
    public Tag deepCopy(Tag value) {
        return null;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Tag value) {
        return null;
    }

    @Override
    public Tag assemble(Serializable cached, Object owner) {
        return null;
    }
}
