package blog;

import blog.domain.Tag;
import blog.repository.TagRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;
import static org.springframework.data.domain.Sort.Direction.ASC;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TagRepositoryTest {

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private
    TagRepository repository;

    private Tag home, street, sky, car, box;
    private Integer id;

    private void flushCategories() {
        home = new Tag("home");
        street = new Tag("street");
        sky = new Tag("sky");
        car = new Tag("car");
        box = new Tag("box");

        this.repository.save(home);
        this.repository.save(street);
        this.repository.save(sky);
        this.repository.save(car);
        this.repository.save(box);

        id = home.getId();

        assertThat(id, is(notNullValue()));
        assertThat(street.getId(), is(notNullValue()));
        assertThat(sky.getId(), is(notNullValue()));
        assertThat(car.getId(), is(notNullValue()));
        assertThat(box.getId(), is(notNullValue()));

        assertThat(repository.exists(home.getId()), is(true));
        assertThat(repository.exists(street.getId()), is(true));
        assertThat(repository.exists(sky.getId()), is(true));
        assertThat(repository.exists(car.getId()), is(true));
        assertThat(repository.exists(box.getId()), is(true));
    }

    @Test
    public void createTest() throws Exception {
        final List<Tag> allCategories = this.repository.findAll();
        Long count = allCategories.stream().count();

        flushCategories();

        assertThat(this.repository.count(), is(count + 5));
    }

    @Test
    public void testRead() throws Exception {
        flushCategories();

        Tag foundCategory = repository.findOne(id);
        assertThat(home.getName(), is(foundCategory.getName()));
    }

    @Test
    public void testReadByIdReturnsNullForNotFoundEntities() throws Exception {
        flushCategories();

        assertThat(repository.findOne(id * 11), is(nullValue()));
    }

    @Test
    public void testSavingEmptyCollectionIsNoOp() throws Exception {
        List<Tag> result = repository.save(new ArrayList<>());
        assertThat(result, is(notNullValue()));
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void testUpdate() throws Exception {
        flushCategories();

        Tag foundCategory = repository.findOne(id);
        foundCategory.setName("super tag");

        Tag updatedCategory = repository.findOne(id);
        assertThat(updatedCategory.getName(), is(foundCategory.getName()));
    }

    @Test
    public void testDelete() throws Exception {
        flushCategories();

        repository.delete(home);
        assertThat(repository.exists(id), is(false));
        assertThat(repository.findOne(id), is(nullValue()));
    }

    @Test
    public void testDeleteTagById() throws Exception {
        flushCategories();

        repository.delete(home.getId());
        assertThat(repository.exists(id), is(false));
        assertThat(repository.findOne(id), is(nullValue()));
    }

    @Test
    public void testFindAllByGivenIds() throws Exception {
        flushCategories();

        Iterable<Tag> result = repository.findAll(Arrays.asList(home.getId(), street.getId()));
        assertThat(result, hasItems(home, street));
    }

    @Test
    public void testReadAll() throws Exception {
        flushCategories();

        assertThat(repository.count(), is(5L));
        assertThat(repository.findAll(), hasItems(home, street, sky, car, box));
    }

    @Test
    public void testDeleteAll() throws Exception {
        flushCategories();

        repository.deleteAll();
        assertThat(repository.count(), is(0L));
    }

    @Test
    public void testReturnsAllSortedCorrectly() throws Exception {
        flushCategories();

        List<Tag> result = repository.findAll(new Sort(ASC, "name"));
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(5));
        assertThat(result.get(0), is(box));
        assertThat(result.get(1), is(car));
        assertThat(result.get(2), is(home));
        assertThat(result.get(3), is(sky));
        assertThat(result.get(4), is(street));
    }

    @Test
    public void testFindByName() throws Exception {
        flushCategories();

        List<Tag> byName = repository.findByName("home");

        assertThat(byName.size(), is(1));
        assertThat(byName.get(0), is(home));
    }

    @Test
    public void testFindByNameNotFound() throws Exception {
        flushCategories();

        List<Tag> byName = repository.findByName("sun");

        assertThat(byName.size(), is(0));
        assertThat(byName.isEmpty(), is(true));
    }

    @Test
    public void testCountsCorrectly() throws Exception {
        long count = repository.count();

        Tag category = new Tag("climate");
        repository.save(category);

        assertThat(repository.count() == count + 1, is(true));
    }

    @Test
    public void testExecutesNotLikeCorrectly() throws Exception {
        flushCategories();

        List<Tag> result = repository.findByNameNotLike("%ee1%");
        assertThat(result.size(), is(5));
        assertThat(result, hasItems(home, street, sky, car, box));
    }
}