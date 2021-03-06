package dev.actions.repository

import dev.actions.AbstractTest
import dev.actions.domain.Item
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import reactor.test.StepVerifier
import java.time.LocalDateTime


/**
 * @author vladov 2019-03-16
 */
@DataMongoTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemRepositoryTest(@Autowired private val itemRepository: ItemRepository) : AbstractTest() {

    @BeforeAll
    internal fun setup() {
        itemRepository.save(Item(null, "desc", "132sda", LocalDateTime.now(), "username")).block()
        itemRepository.save(Item(null, "addescda", "sda321", LocalDateTime.now(), "username")).block()
        itemRepository.save(Item(null, "dasda", "das", LocalDateTime.now(), "username111")).block()
    }

    @Test
    fun testFindByDescriptionContainingAndUsernameSuccess() {
        val items = itemRepository.findByDescriptionContainingAndUsername("desc", "username")
        StepVerifier
                .create(items)
                .assertNext { item ->
                    assertThat(item).isNotNull
                    assertThat(item.description).isEqualTo("desc")
                }
                .assertNext { item ->
                    assertThat(item.description).isEqualTo("addescda")
                }
                .expectComplete()
                .verify()
    }

    @Test
    fun testFindByDescriptionContainingAndUsernameEmptyResult() {
        val items = itemRepository.findByDescriptionContainingAndUsername("desc", "vasya")
        StepVerifier
                .create(items)
                .expectComplete()
                .verify()
    }

    @Test
    fun testFindByResultContainingAndUsernameSuccess() {
        val items = itemRepository.findByDescriptionContainingAndUsername("desc", "username")
        StepVerifier
                .create(items)
                .assertNext { item ->
                    assertThat(item).isNotNull
                    assertThat(item.description).isEqualTo("desc")
                }
                .assertNext { item ->
                    assertThat(item.description).isEqualTo("addescda")
                }
                .expectComplete()
                .verify()
    }

    @Test
    fun testFindByResultContainingAndUsernameEmptyResult() {
        val items = itemRepository.findByResultContainingAndUsername("desc", "vasya")
        StepVerifier
                .create(items)
                .expectComplete()
                .verify()
    }

    @Test
    fun testFindByIdSuccess() {
        itemRepository.save(Item("123321", "dasda", "das", LocalDateTime.now(), "username111")).block()
        val items = itemRepository.findById("123321")
        StepVerifier
                .create(items)
                .assertNext { item ->
                    assertThat(item).isNotNull
                    assertThat(item.id).isEqualTo("123321")
                    assertThat(item.description).isEqualTo("dasda")
                    assertThat(item.result).isEqualTo("das")
                }
                .expectComplete()
                .verify()
    }

    @Test
    fun testFindByIdEmptyResult() {
        val items = itemRepository.findById("1")
        StepVerifier
                .create(items)
                .expectComplete()
                .verify()
    }

    @Test
    fun testFindTimestampBetweenSuccess() {
        itemRepository.save(Item(null, "1", "1res", LocalDateTime.of(2000, 1, 1, 5, 5), "username111")).block()
        itemRepository.save(Item(null, "2", "2res", LocalDateTime.of(2000, 1, 1, 6, 5), "username111")).block()
        itemRepository.save(Item(null, "3", "3res", LocalDateTime.of(2000, 1, 1, 7, 5), "username111")).block()
        val startDate = LocalDateTime.of(2000, 1, 1, 0, 0)
        val endDate = LocalDateTime.of(2000, 1, 2, 0, 0, 0)
        val items = itemRepository.findByTimestampBetweenAndUsernameOrderByTimestamp(startDate, endDate, "username111")
        StepVerifier
                .create(items)
                .assertNext { item ->
                    assertThat(item).isNotNull
                    assertThat(item.description).isEqualTo("1")
                    assertThat(item.result).isEqualTo("1res")
                }
                .assertNext { item ->
                    assertThat(item).isNotNull
                    assertThat(item.description).isEqualTo("2")
                    assertThat(item.result).isEqualTo("2res")
                }
                .assertNext { item ->
                    assertThat(item).isNotNull
                    assertThat(item.description).isEqualTo("3")
                    assertThat(item.result).isEqualTo("3res")
                }
                .expectComplete()
                .verify()
    }

    @Test
    fun testFindTimestampBetweenEmptyResult() {
        val startDate = LocalDateTime.of(1999, 1, 1, 0, 0)
        val endDate = LocalDateTime.of(1999, 1, 2, 0, 0, 0)
        val items = itemRepository.findByTimestampBetweenAndUsernameOrderByTimestamp(startDate, endDate, "username111")
        StepVerifier
                .create(items)
                .expectComplete()
                .verify()
    }
}
