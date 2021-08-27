import { CommandDescriptor } from "@/model/transport/CommandDescriptor";
import { ContextService } from "@/services/payload/ContextService";


class ContextServiceMock implements ContextService {

    getAvailableCommandDescriptorsInContext(): Promise<CommandDescriptor[]> {
        return new Promise<any>((resolve, reject) => {
            resolve([]);
        });
    }

    async getContext(command: string): Promise<any> {

        return new Promise<any>((resolve, reject) => {

            if (command === 'click') {
                resolve(contextClick);
            } else if (command === 'write') {
                resolve(contextWrite);
            } else {
                resolve({});
            }
        })

    }
}

export { ContextServiceMock }

const contextClick = {
    "elementSuperLocator": [
        {
            "id": "c3c2c672-cdae-4b78-8cd5-9e4543b6ba9d",
            "screenshot": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAASwAAAAjCAIAAAChPqBDAAADtUlEQVR4Xu2dz0tiURTH/fv6M/oD2j0iIoSwJouykn5BUES0jFxEtihpSmOCNkE5QhTDCLlIp9RH+sa8c+admTO3Z+PT0Hc33w+3uud7zrndxfvm9boo9KPWHEw8Y2BgBD8+patKqRDNFADABDPndupbHSYEwBgVRw0lyzAhACYhA8KEAJgEJgTAMDAhAIaBCQEwjI8JBwYGvBIAoKf4mzDv4k0oVa1WSXccx5sAAHSDvwktF29CqUQiQfrV1ZU3AQDoBpgQAMPAhAAYpmcmLBQKXMmcnZ3pxdfX15IKh8N6ipR0Oi1ZVk5OTjY2NkR8eHjQW1ZXVyVF3N/fS8pyf3U8HpcsibZtS7i3tyfFqu3GAAiG3piw0Whw2fr6+ubmJs+z2SxXLi8vS5YedJ7LOhwKukLFw8PDPKdfodeTvrOzMzU1xSHZzLPa6Oio9DITExM8ubu74+L2GwMgGHpjwmQySfNarcapl5cX6RJ/SmM+n6cwGo1yyFlqkQJWxHXHx8cUkreVeyWr9xK5XE6y0lsul/UwEolw+PT0ROHS0pLqYGMABENvTHh7e8tlNPGUHR4ekn5zc6OL+pqt67+rzM/PS/j6+irzUqlEWXpN49DTS46ytL8OXDA2NqY62BgAwfBxE25vb+sPMR3/uJI5OjpifXZ2Vtd1uMByD4o8F4XOmR5lbm5OQjnxCroJ9d5YLGa1+JlN6LsxAILh4yacnJwkvVAoiEKHwLW1NXmU6S0Zify2Lfke3EXZeDwui7BC5vEobEJ6DZT1aWU6qTabTeutCfXeNib03RgAweBvQr7eoDdjnhQ7geeVSkW/DmVjcHZ/f99qOfVdXFzIhafVjQm3trZonkqlJPX4+Kiv4OltY0LfjQEQDP4mzGQy7Ciylui7u7sscsjPutw6Ks2ijuPolQQ95bpidWPCSCRC8+fnfxvmpehsKWGHJvTdGADB4G9C+u656xeKxSKX1et1VsLhsJxI5U5yYWGBlenp6fHxcZ6XSiXOWt2YMJvNcju9JMrnE9bfo29rbxsTKr+NARAMHZmQOD09/fO8uywuLupXlMr9WGJkZEQKDg4O9KxcnxJU5rmuXFlZ0Wp/K2SP/ymXl5eyFIs8b60kaJ+SYqy3H8q32RgAwdCpCQEAfQImBMAwMCEAhoEJATAMTAiAYXxMCADoNzAhAIaBCQEwDEwIgGFgQgBMkis2Zs7tUDRj0w/7pzcNAOgrX4sNfgkM0dfn785QsjzY8m9EMTAw+jdiX2x24y+Q8XTA+61D+QAAAABJRU5ErkJggg==",
            "asString": "<input autocorrect=\"off\" autocomplete=\"off\" name=\"j_username\" id=\"j_username\" placeholder=\"Username\" type=\"text\" class=\"normal\" autocapitalize=\"off\" aria-label=\"Username\">",
            "locators": [],
            "tags": [
                "write",
                "click",
                "write",
                "click",
                "write",
                "click",
                "write",
                "click",
                "write",
                "click",
                "write",
                "click",
                "write",
                "click"
            ],
            "bounds": {
                "x": 810.0,
                "y": 504.0,
                "height": 35.0,
                "width": 300.0,
                "size": 10500.0
            }
        },
        {
            "id": "da529ec2-852e-43ff-a0fe-85a7b16c4bf9",
            "screenshot": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAASwAAAAjCAIAAAChPqBDAAAD0klEQVR4Xu2c7SssURzH/dOTJClRvNg2yVMrSRJeqKu0Eu4bwgualSFh980tj+txx5Jh7u/ut/nds4OxV8u56vt5oe/v4ZzdrfnuOWcGDeVy+SchxAau64Zh2CAqJITYoFAoFItFmpAQawRB4HkeTUiITXK5HE1IiE1oQkIsQxMSYhmakBDL0ISEWOZ9E/6q5ujoyPf9eNN3Rj7U9fV1PEvIV/G+CZ03aGtri7d+T+SzjIyMxLOEfBW1mnA3Ynt7O5vNIjk8PBzv/obQhMQutZowlnx6eno1/x2hCYldPmjCl/mBgQFkQKFQ0NLx8bFZWl5eTi49Pj6KTqfT2oaq5M1Me3s79Orqqs7Q0tKiPWhbWVnRKpKygGsGr0UTEot80ITPz89mHrqxsXF8fLyzsxMh7t8EQYBwaGhIrnVo2dYml6D15RDOzMwg3NzclHB/f190KpVCNZPJNDc3vzpQkYy8SWjTijQhscg/m1CWjq2tLb2OJVMqlUR3dXVpz97enl7Zc3Nzou/v71Eql8s6YUJJVjmIMJpfS0JTUxP06empmRc8z5NQGhCiKjMjPD8/dypfFtqPBpqQWKRWE77EvJTDyilR9cXFhTT09fWJPjg4QL+Iv90VEkqym3Wi3enY2JhorHKoYpSIyclJEVdXV+ZYrca0IG9JwtvbW80Ui0WHJiRWqdWEPyJmZ2fX19dNywHdTyowoaC7RLC4uKijEkpO5HOUxJDyU1bFy8tLEWtrazpchwBdJ8PK2I6OjldLikMTEqvUasJ41kDvlApyIFxaWsKJUU0o3NzcDA4Oapu5ir5VQggxNTUFMTEx0dvbi3z4hglx6oMW0dPToyWakPyH1MGEo6Oj0mBOgqMXrn7Z+5m3Q807OgmlMLrnubu760SHOlTNHuxUz87OdBJtU22aMJPJSObk5EQzvu87NCGxSh1MiJso5m9+YUgqlRKdTqdF5/P5WDW5ZIaakY0lQtkSIyM2jg3Z2NgwM061Ce/u7mL9WDZpQmKROpgQi5UgS6I+n3CijeXDwwPC1tZW3XbiEV9CCSDjRK++s7NjhgBfAU7luaI+ftAjq1NtwtBwsuQhHJqQWKUOJgyN9Ufo7u4Oq0fJZhKHMZDNZnVgQkmYnp6W5Pz8vGbQZrT8wXVdncF8xB9W+vv7+82MsLCwgGYxLR7Wy7Y21kPIl/G+CQkhnwpNSIhlaEJCLEMTEmIZmpAQy9CEhFiGJiTEMjQhIZahCQmxDE1IiE183z88PGxwXTefzwdBEK8TQj6TUqkky6CIhrDyt/Ce5+UIIV+I/j+035H2fLmyNAxBAAAAAElFTkSuQmCC",
            "asString": "<input name=\"j_password\" placeholder=\"Password\" type=\"password\" class=\"normal\" aria-label=\"Password\">",
            "locators": [],
            "tags": [
                "write",
                "click",
                "write",
                "click",
                "write",
                "click",
                "write",
                "click",
                "write",
                "click",
                "write",
                "click",
                "write",
                "click"
            ],
            "bounds": {
                "x": 810.0,
                "y": 555.0,
                "height": 35.0,
                "width": 300.0,
                "size": 10500.0
            }
        },
        {
            "id": "ea9cf565-260d-4d9d-8da3-3803b66afacd",
            "screenshot": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA0AAAABCAIAAACKS7NQAAAAIElEQVR4XmP4Dwatra1eXl4QNhAA2d++fYOwS0tLgVwAbBodVrhHX8wAAAAASUVORK5CYII=",
            "asString": "<input type=\"checkbox\" id=\"remember_me\" name=\"remember_me\">",
            "locators": [],
            "tags": [
                "write",
                "click",
                "write",
                "click",
                "write",
                "click"
            ],
            "bounds": {
                "x": 950.0,
                "y": 672.0,
                "height": 1.0,
                "width": 13.0,
                "size": 13.0
            }
        }
    ]
};

const contextWrite = {
    "elementSuperLocator": [
        {
            "id": "c3c2c672-cdae-4b78-8cd5-9e4543b6ba9d",
            "screenshot": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAASwAAAAjCAIAAAChPqBDAAADtUlEQVR4Xu2dz0tiURTH/fv6M/oD2j0iIoSwJouykn5BUES0jFxEtihpSmOCNkE5QhTDCLlIp9RH+sa8c+admTO3Z+PT0Hc33w+3uud7zrndxfvm9boo9KPWHEw8Y2BgBD8+patKqRDNFADABDPndupbHSYEwBgVRw0lyzAhACYhA8KEAJgEJgTAMDAhAIaBCQEwjI8JBwYGvBIAoKf4mzDv4k0oVa1WSXccx5sAAHSDvwktF29CqUQiQfrV1ZU3AQDoBpgQAMPAhAAYpmcmLBQKXMmcnZ3pxdfX15IKh8N6ipR0Oi1ZVk5OTjY2NkR8eHjQW1ZXVyVF3N/fS8pyf3U8HpcsibZtS7i3tyfFqu3GAAiG3piw0Whw2fr6+ubmJs+z2SxXLi8vS5YedJ7LOhwKukLFw8PDPKdfodeTvrOzMzU1xSHZzLPa6Oio9DITExM8ubu74+L2GwMgGHpjwmQySfNarcapl5cX6RJ/SmM+n6cwGo1yyFlqkQJWxHXHx8cUkreVeyWr9xK5XE6y0lsul/UwEolw+PT0ROHS0pLqYGMABENvTHh7e8tlNPGUHR4ekn5zc6OL+pqt67+rzM/PS/j6+irzUqlEWXpN49DTS46ytL8OXDA2NqY62BgAwfBxE25vb+sPMR3/uJI5OjpifXZ2Vtd1uMByD4o8F4XOmR5lbm5OQjnxCroJ9d5YLGa1+JlN6LsxAILh4yacnJwkvVAoiEKHwLW1NXmU6S0Zify2Lfke3EXZeDwui7BC5vEobEJ6DZT1aWU6qTabTeutCfXeNib03RgAweBvQr7eoDdjnhQ7geeVSkW/DmVjcHZ/f99qOfVdXFzIhafVjQm3trZonkqlJPX4+Kiv4OltY0LfjQEQDP4mzGQy7Ciylui7u7sscsjPutw6Ks2ijuPolQQ95bpidWPCSCRC8+fnfxvmpehsKWGHJvTdGADB4G9C+u656xeKxSKX1et1VsLhsJxI5U5yYWGBlenp6fHxcZ6XSiXOWt2YMJvNcju9JMrnE9bfo29rbxsTKr+NARAMHZmQOD09/fO8uywuLupXlMr9WGJkZEQKDg4O9KxcnxJU5rmuXFlZ0Wp/K2SP/ymXl5eyFIs8b60kaJ+SYqy3H8q32RgAwdCpCQEAfQImBMAwMCEAhoEJATAMTAiAYXxMCADoNzAhAIaBCQEwDEwIgGFgQgBMkis2Zs7tUDRj0w/7pzcNAOgrX4sNfgkM0dfn785QsjzY8m9EMTAw+jdiX2x24y+Q8XTA+61D+QAAAABJRU5ErkJggg==",
            "asString": "<input autocorrect=\"off\" autocomplete=\"off\" name=\"j_username\" id=\"j_username\" placeholder=\"Username\" type=\"text\" class=\"normal\" autocapitalize=\"off\" aria-label=\"Username\">",
            "locators": [],
            "tags": [
                "write",
                "click",
                "write",
                "click",
                "write",
                "click",
                "write",
                "click",
                "write",
                "click",
                "write",
                "click",
                "write",
                "click"
            ],
            "bounds": {
                "x": 810.0,
                "y": 504.0,
                "height": 35.0,
                "width": 300.0,
                "size": 10500.0
            }
        },
        {
            "id": "da529ec2-852e-43ff-a0fe-85a7b16c4bf9",
            "screenshot": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAASwAAAAjCAIAAAChPqBDAAAD0klEQVR4Xu2c7SssURzH/dOTJClRvNg2yVMrSRJeqKu0Eu4bwgualSFh980tj+txx5Jh7u/ut/nds4OxV8u56vt5oe/v4ZzdrfnuOWcGDeVy+SchxAau64Zh2CAqJITYoFAoFItFmpAQawRB4HkeTUiITXK5HE1IiE1oQkIsQxMSYhmakBDL0ISEWOZ9E/6q5ujoyPf9eNN3Rj7U9fV1PEvIV/G+CZ03aGtri7d+T+SzjIyMxLOEfBW1mnA3Ynt7O5vNIjk8PBzv/obQhMQutZowlnx6eno1/x2hCYldPmjCl/mBgQFkQKFQ0NLx8bFZWl5eTi49Pj6KTqfT2oaq5M1Me3s79Orqqs7Q0tKiPWhbWVnRKpKygGsGr0UTEot80ITPz89mHrqxsXF8fLyzsxMh7t8EQYBwaGhIrnVo2dYml6D15RDOzMwg3NzclHB/f190KpVCNZPJNDc3vzpQkYy8SWjTijQhscg/m1CWjq2tLb2OJVMqlUR3dXVpz97enl7Zc3Nzou/v71Eql8s6YUJJVjmIMJpfS0JTUxP06empmRc8z5NQGhCiKjMjPD8/dypfFtqPBpqQWKRWE77EvJTDyilR9cXFhTT09fWJPjg4QL+Iv90VEkqym3Wi3enY2JhorHKoYpSIyclJEVdXV+ZYrca0IG9JwtvbW80Ui0WHJiRWqdWEPyJmZ2fX19dNywHdTyowoaC7RLC4uKijEkpO5HOUxJDyU1bFy8tLEWtrazpchwBdJ8PK2I6OjldLikMTEqvUasJ41kDvlApyIFxaWsKJUU0o3NzcDA4Oapu5ir5VQggxNTUFMTEx0dvbi3z4hglx6oMW0dPToyWakPyH1MGEo6Oj0mBOgqMXrn7Z+5m3Q807OgmlMLrnubu760SHOlTNHuxUz87OdBJtU22aMJPJSObk5EQzvu87NCGxSh1MiJso5m9+YUgqlRKdTqdF5/P5WDW5ZIaakY0lQtkSIyM2jg3Z2NgwM061Ce/u7mL9WDZpQmKROpgQi5UgS6I+n3CijeXDwwPC1tZW3XbiEV9CCSDjRK++s7NjhgBfAU7luaI+ftAjq1NtwtBwsuQhHJqQWKUOJgyN9Ufo7u4Oq0fJZhKHMZDNZnVgQkmYnp6W5Pz8vGbQZrT8wXVdncF8xB9W+vv7+82MsLCwgGYxLR7Wy7Y21kPIl/G+CQkhnwpNSIhlaEJCLEMTEmIZmpAQy9CEhFiGJiTEMjQhIZahCQmxDE1IiE183z88PGxwXTefzwdBEK8TQj6TUqkky6CIhrDyt/Ce5+UIIV+I/j+035H2fLmyNAxBAAAAAElFTkSuQmCC",
            "asString": "<input name=\"j_password\" placeholder=\"Password\" type=\"password\" class=\"normal\" aria-label=\"Password\">",
            "locators": [],
            "tags": [
                "write",
                "click",
                "write",
                "click",
                "write",
                "click",
                "write",
                "click",
                "write",
                "click",
                "write",
                "click",
                "write",
                "click"
            ],
            "bounds": {
                "x": 810.0,
                "y": 555.0,
                "height": 35.0,
                "width": 300.0,
                "size": 10500.0
            }
        },
        {
            "id": "ea9cf565-260d-4d9d-8da3-3803b66afacd",
            "screenshot": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA0AAAABCAIAAACKS7NQAAAAIElEQVR4XmP4Dwatra1eXl4QNhAA2d++fYOwS0tLgVwAbBodVrhHX8wAAAAASUVORK5CYII=",
            "asString": "<input type=\"checkbox\" id=\"remember_me\" name=\"remember_me\">",
            "locators": [],
            "tags": [
                "write",
                "click",
                "write",
                "click",
                "write",
                "click"
            ],
            "bounds": {
                "x": 950.0,
                "y": 672.0,
                "height": 1.0,
                "width": 13.0,
                "size": 13.0
            }
        }
    ]
};