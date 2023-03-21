package biz.digissance.graalvmdemo.jpa.base;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.dialect.PostgreSQLPGObjectJdbcType;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;

@Slf4j
public class BaseEntityImportBeanDefinitionRegistrar implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(final RuntimeHints hints, final ClassLoader classLoader) {

        hints.reflection().registerTypes(List.of(TypeReference.of(BaseEntityListener.class)),
                hint -> hint
                        .withMembers(
                                MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
                                MemberCategory.DECLARED_FIELDS,
                                MemberCategory.INTROSPECT_DECLARED_METHODS,
                                MemberCategory.INVOKE_DECLARED_METHODS));


        hints.reflection().registerTypeIfPresent(classLoader, "org.postgresql.util.PGobject",
                (hint) -> hint.withMembers(MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS, MemberCategory.INTROSPECT_PUBLIC_METHODS)
                        .onReachableType(PostgreSQLPGObjectJdbcType.class));

        log.info("Registering BaseEntityListener runtime hints");
    }
}
