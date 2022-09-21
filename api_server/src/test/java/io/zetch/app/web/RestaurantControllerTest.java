package io.zetch.app.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.zetch.app.domain.Restaurant;
import io.zetch.app.domain.RestaurantDto;
import io.zetch.app.service.RestaurantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RestaurantController.class)
public class RestaurantControllerTest {

    private static final String RESTAURANT_ENDPOINT = "/restaurants/";

    private static final Long ID_1 = 1L;
    private static final String NAME_1 = "Bob's";
    private static final String CUISINE_1 = "Italian";
    private static final String ADDRESS_1 = "1234 Broadway";
    private static final Long ID_2 = 2L;
    private static final String NAME_2 = "Cat's";
    private static final String CUISINE_2 = "French";
    private static final String ADDRESS_2 = "15 Amsterdam";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    private RestaurantService restaurantServiceMock;

    Restaurant r1 = new Restaurant(ID_1, NAME_1, CUISINE_1, ADDRESS_1);
    Restaurant r2 = new Restaurant(ID_2, NAME_2, CUISINE_2, ADDRESS_2);

    @Test
    public void getAllRestaurants() throws Exception {
        when(restaurantServiceMock.getAll()).thenReturn(List.of(r1, r2));

        mockMvc.perform(get(RESTAURANT_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("*", notNullValue()))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].*", hasSize(3)))  // Make sure ID is not returned
                .andExpect(jsonPath("$[0].name", is(NAME_1)))
                .andExpect(jsonPath("$[1].name", is(NAME_2)));
    }

    @Test
    public void getRestaurantById() throws Exception {
        when(restaurantServiceMock.getOne(r1.getId())).thenReturn(r1);

        mockMvc.perform(get(RESTAURANT_ENDPOINT + r1.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("*", notNullValue()))
                .andExpect(jsonPath("$.name", is(r1.getName())))
                .andExpect(jsonPath("$.cuisine", is(r1.getCuisine())))
                .andExpect(jsonPath("$.address", is(r1.getAddress())));
    }

    @Test
    public void createRestaurant() throws Exception {
        when(restaurantServiceMock.createNew(r1.getName(), r1.getCuisine(), r1.getAddress())).thenReturn(r1);

        MockHttpServletRequestBuilder mockRequest = post(RESTAURANT_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new RestaurantDto(r1.getName(), r1.getCuisine(), r1.getAddress())));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("*", notNullValue()))
                .andExpect(jsonPath("$.name", is(r1.getName())))
                .andExpect(jsonPath("$.cuisine", is(r1.getCuisine())))
                .andExpect(jsonPath("$.address", is(r1.getAddress())));
    }
}
