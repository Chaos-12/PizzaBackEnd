package com.example.demo.core.configurationBeans;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.demo.domain.userDomain.Role;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.MySqlDialect;

@Configuration
public class CustomConverters {
    @Bean
    public R2dbcCustomConversions customConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new UUIDToByteArrayConverter());
        converters.add(new ByteArrayToUUIDConverter());
        converters.add(new RolToIntegerConverter());
        converters.add(new IntegerToRolConverter());
        return R2dbcCustomConversions.of(MySqlDialect.INSTANCE, converters);
    }

    @WritingConverter
    private class UUIDToByteArrayConverter implements Converter<UUID, byte[]> {
        @Override
        public byte[] convert(UUID source) {
            ByteBuffer bb = ByteBuffer.wrap(new byte[16]).order(ByteOrder.BIG_ENDIAN);
            bb.putLong(source.getMostSignificantBits());
            bb.putLong(source.getLeastSignificantBits());
            return bb.array();
        }
    }

    @ReadingConverter
    public class ByteArrayToUUIDConverter implements Converter<byte[], UUID> {
        @Override
        public UUID convert(byte[] source) {
            ByteBuffer bytebuffer = ByteBuffer.wrap(source);
            Long high = bytebuffer.getLong();
            Long low = bytebuffer.getLong();
            return new UUID(high, low);
        }
    }
    
    @WritingConverter
    private class RolToIntegerConverter implements Converter<Role, Integer> {
		@Override
		public Integer convert(Role role) {
			return role.ordinal();
		}
	}

    @ReadingConverter
	public class IntegerToRolConverter implements Converter<Integer,Role> {
		@Override
		public Role convert(Integer value) {
			return Role.values()[value];
		}
	}
}