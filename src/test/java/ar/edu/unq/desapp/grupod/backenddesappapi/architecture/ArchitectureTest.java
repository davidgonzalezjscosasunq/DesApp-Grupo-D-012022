package ar.edu.unq.desapp.grupod.backenddesappapi.architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses (packages = "ar.edu.unq.desapp.grupod.backenddesappapi")
public class ArchitectureTest {

    @ArchTest
    public static final ArchRule serviceClassesShouldOnlyBeAccessedByControllersAndAspectsPackages = classes()
            .that().resideInAPackage ("..service..")
            .should().onlyBeAccessed().byAnyPackage("..controller..", "..service..", "..aspects..");

    @ArchTest
    public static final ArchRule controllerClassesShouldNotBeAccessedByAnyOtherPackage = classes()
            .that().resideInAPackage("..controller..")
            .should().onlyBeAccessed().byAnyPackage("..controller..");

    @ArchTest
    public static final ArchRule persistenceClassesShouldOnlyBeAccessedByServicePackage = classes()
            .that().resideInAPackage("..persistence..")
            .should().onlyBeAccessed().byAnyPackage("..service..", "..persistence..");

    @ArchTest
    public static final ArchRule classesWithNameServiceAtTheEndShouldBeOnlyInServicePackage = classes()
            .that().haveSimpleNameEndingWith("Service")
            .should().resideInAPackage("..service..");

    @ArchTest
    public static final ArchRule classesWithNameControllerAtTheEndShouldBeOnlyInControllerPackage = classes()
            .that().haveSimpleNameEndingWith("Controller")
            .should().resideInAPackage("..controller..");

    @ArchTest
    public static final ArchRule classesWithNameRepositoryAtTheEndShouldBeOnlyInPersistencePackage = classes()
            .that().haveSimpleNameEndingWith("Repository")
            .should().resideInAPackage("..persistence..");

    @ArchTest
    public static final ArchRule classesWithNameDTOAtTheEndShouldBeOnlyInDtosPackage = classes()
            .that().haveSimpleNameEndingWith("DTO")
            .should().resideInAPackage("..controller.dtos..");

    @ArchTest
    public static final ArchRule classesWithNameValidatorAtTheEndShouldBeOnlyInValidatorsPackage = classes()
            .that().haveSimpleNameEndingWith("Validator")
            .should().resideInAPackage("..model.validators..");

    @ArchTest
    public static final ArchRule repositoryClassesShouldBeInterfaces = classes()
            .that().haveSimpleNameEndingWith("Repository")
            .should().beInterfaces();

    @ArchTest
    public static final ArchRule classesWithTransactionalAnnotationShouldBeInServicePackage = classes()
            .that().areAnnotatedWith(Transactional.class)
            .should().resideInAPackage("..service..");

    @ArchTest
    public static final ArchRule classesWithRestControllerAnnotationShouldBeInControllerPackage = classes()
            .that().areAnnotatedWith(RestController.class)
            .should().resideInAPackage("..controller..");

    @ArchTest
    public static final ArchRule classesWithRepositoryAnnotationShouldBeInPersistencePackage = classes()
            .that().areAnnotatedWith(Repository.class)
            .should().resideInAPackage("..persistence..");

    @ArchTest
    public static final ArchRule classesWithEntityAnnotationShouldBeInModelPackage = classes()
            .that().areAnnotatedWith(Entity.class)
            .should().resideInAPackage("..model..");

    @ArchTest
    public static final ArchRule classesWithConfigurationAnnotationShouldBeInConfigurationPackage = classes()
            .that().areAnnotatedWith(Configuration.class)
            .should().resideInAPackage("..configuration..");

    @ArchTest
    public static final ArchRule classesWithAspectAnnotationShouldBeInAspectsPackage = classes()
            .that().areAnnotatedWith(Aspect.class)
            .should().resideInAPackage("..aspects..");

    @ArchTest
    public static final ArchRule classesWithServiceAnnotationShouldBeInServicePackage = classes()
            .that().areAnnotatedWith(Service.class)
            .should().resideInAnyPackage("..service..", "..model.clock.."); //hay que mover el clock?

}
