import { afterEach, describe, expect, test, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import Filterchat from '../components/Filterchat.vue'


describe('Filter chat', () => {
    
    afterEach(() => {
        vi.unstubAllGlobals()
    });

    test('fetch and display no messages when filter is empty', async () => {

        const wrapper = mount(Filterchat, {
            global: {
                mocks: {
                    $route: {
                        query: {}
                    }
                }
            }
        })
        await wrapper.vm.$nextTick()

        const messages = wrapper.findAll('.searchedMessage')

        expect(messages.length).toBe(0);
    });

    test('fetch and display messages when a username is selected', async () => {
        const wrapper = mount(Filterchat, {
            global: {
                mocks: {
                    $route: {
                        query: {}
                    }
                }
            }
        })
        await wrapper.vm.$nextTick()

        wrapper.setProps({
            keywordArray: [
                { word: '@user1', isUser: true, isCustomerId: false }
            ]
        })

        const mockData = [
            [1, "SN123", "Hello", "2023-01-01T00:00:00", null, "User1", "User1"],
        ];

        const _useFetch = vi.fn().mockImplementation((url: string) => {
            return {data: ref(mockData)};
        });

        vi.stubGlobal('useFetch', _useFetch);

        await wrapper.vm.$nextTick()
        await wrapper.vm.$nextTick()
        
        // find the messages that are displayed
        const messages = wrapper.findAll('.searchedMessage')

        // test that the correct number of messages are displayed
        expect(messages.length).toBe(1);
        const firstMessage = messages[0];
        expect(firstMessage.find('.messagesender').text()).toBe('User1:');
        expect(firstMessage.find('.messageContent').text()).toBe('Hello');
        expect(firstMessage.find('.dateTime').text()).toBe('1/1/2023, 12:00:00 AM');
        expect(firstMessage.find('.flagged').exists()).toBe(false);
        
        // test that the correct url is called with the correct parameters
        expect(_useFetch).toHaveBeenCalledWith('http://localhost:8080/search/fulltext', { 
            query: { 
                keywords: "",
                username: "user1",
                dateTimeFrom: null,
                dateTimeTo: null,
                customerId: "",
                isFlagged: false
            }
        })
    });

    test('fetch and display messages when a customer id is selected', async () => {
        const wrapper = mount(Filterchat, {
            global: {
                mocks: {
                    $route: {
                        query: {}
                    }
                }
            }
        })
        await wrapper.vm.$nextTick()

        wrapper.setProps({
            keywordArray: [
                { word: 'SN123', isUser: false, isCustomerId: true }
            ]
        })

        const mockData = [
            [1, "SN123", "Hello", "2023-01-01T00:00:00", null, "User1", "User1"],
        ];

        const _useFetch = vi.fn().mockImplementation((url: string) => {
            return {data: ref(mockData)};
        });

        vi.stubGlobal('useFetch', _useFetch);

        await wrapper.vm.$nextTick()
        await wrapper.vm.$nextTick()

        
        // find the messages that are displayed
        const messages = wrapper.findAll('.searchedMessage')

        // test that the correct number of messages are displayed
        expect(messages.length).toBe(1);
        const firstMessage = messages[0];
        expect(firstMessage.find('.messagesender').text()).toBe('User1:');
        expect(firstMessage.find('.messageContent').text()).toBe('Hello');
        expect(firstMessage.find('.dateTime').text()).toBe('1/1/2023, 12:00:00 AM');
        expect(firstMessage.find('.flagged').exists()).toBe(false);
        
        // test that the correct url is called with the correct parameters
        expect(_useFetch).toHaveBeenCalledWith('http://localhost:8080/search/fulltext', { 
            query: { 
                keywords: "",
                username: "",
                dateTimeFrom: null,
                dateTimeTo: null,
                customerId: "SN123",
                isFlagged: false
            }
        })
    });

    test('fetch and display messages when a keyword is selected', async () => {
        const wrapper = mount(Filterchat, {
            global: {
                mocks: {
                    $route: {
                        query: {}
                    }
                }
            }
        })
        await wrapper.vm.$nextTick()

        wrapper.setProps({
            keywordArray: [
                { word: 'Hello', isUser: false, isCustomerId: false }
            ]
        })

        const mockData = [
            [1, "SN123", "Hello", "2023-01-01T00:00:00", null, "User1", "User1"],
        ];

        const _useFetch = vi.fn().mockImplementation((url: string) => {
            return {data: ref(mockData)};
        });

        vi.stubGlobal('useFetch', _useFetch);

        await wrapper.vm.$nextTick()
        await wrapper.vm.$nextTick()
        
        // find the messages that are displayed
        const messages = wrapper.findAll('.searchedMessage')

        // test that the correct number of messages are displayed
        expect(messages.length).toBe(1);
        const firstMessage = messages[0];
        expect(firstMessage.find('.messagesender').text()).toBe('User1:');
        expect(firstMessage.find('.messageContent').text()).toBe('Hello');
        expect(firstMessage.find('.dateTime').text()).toBe('1/1/2023, 12:00:00 AM');
        expect(firstMessage.find('.flagged').exists()).toBe(false);
        
        // test that the correct url is called with the correct parameters
        expect(_useFetch).toHaveBeenCalledWith('http://localhost:8080/search/fulltext', { 
            query: { 
                keywords: "Hello",
                username: "",
                dateTimeFrom: null,
                dateTimeTo: null,
                customerId: "",
                isFlagged: false
            }
        })
    });

    test('fetch and display messages when a date is selected', async () => {
        const wrapper = mount(Filterchat, {
            global: {
                mocks: {
                    $route: {
                        query: {}
                    }
                }
            }
        })
        await wrapper.vm.$nextTick()

        wrapper.setProps({
            dateTimeObject: {
                dateTimeFrom: "2023-01-01T00:00:00",
                dateTimeTo: ""
            }
        })

        const mockData = [
            [1, "SN123", "Hello", "2023-01-01T00:00:00", null, "User1", "User1"],
        ];

        const _useFetch = vi.fn().mockImplementation((url: string) => {
            return {data: ref(mockData)};
        });

        vi.stubGlobal('useFetch', _useFetch);

        await wrapper.vm.$nextTick()
        await wrapper.vm.$nextTick()
        
        // find the messages that are displayed
        const messages = wrapper.findAll('.searchedMessage')

        // test that the correct number of messages are displayed
        expect(messages.length).toBe(1);
        const firstMessage = messages[0];
        expect(firstMessage.find('.messagesender').text()).toBe('User1:');
        expect(firstMessage.find('.messageContent').text()).toBe('Hello');
        expect(firstMessage.find('.dateTime').text()).toBe('1/1/2023, 12:00:00 AM');
        expect(firstMessage.find('.flagged').exists()).toBe(false);
        
        // test that the correct url is called with the correct parameters
        expect(_useFetch).toHaveBeenCalledWith('http://localhost:8080/search/fulltext', { 
            query: { 
                keywords: "",
                username: "",
                dateTimeFrom: "2023-01-01T00:00:00",
                dateTimeTo: "",
                customerId: "",
                isFlagged: false
            }
        })
    });

    test('fetch and display messages when a date range is selected', async () => {
        const wrapper = mount(Filterchat, {
            global: {
                mocks: {
                    $route: {
                        query: {}
                    }
                }
            }
        })
        await wrapper.vm.$nextTick()

        wrapper.setProps({
            dateTimeObject: {
                dateTimeFrom: "2023-01-01T00:00:00",
                dateTimeTo: "2023-01-02T00:00:00"
            }
        })

        const mockData = [
            [1, "SN123", "Hello", "2023-01-01T00:00:00", null, "User1", "User1"],
        ];

        const _useFetch = vi.fn().mockImplementation((url: string) => {
            return {data: ref(mockData)};
        });

        vi.stubGlobal('useFetch', _useFetch);

        await wrapper.vm.$nextTick()
        await wrapper.vm.$nextTick()

        // find the messages that are displayed
        const messages = wrapper.findAll('.searchedMessage')

        // test that the correct number of messages are displayed
        expect(messages.length).toBe(1);
        const firstMessage = messages[0];
        expect(firstMessage.find('.messagesender').text()).toBe('User1:');
        expect(firstMessage.find('.messageContent').text()).toBe('Hello');
        expect(firstMessage.find('.dateTime').text()).toBe('1/1/2023, 12:00:00 AM');
        expect(firstMessage.find('.flagged').exists()).toBe(false);
        
        // test that the correct url is called with the correct parameters
        expect(_useFetch).toHaveBeenCalledWith('http://localhost:8080/search/fulltext', { 
            query: { 
                keywords: "",
                username: "",
                dateTimeFrom: "2023-01-01T00:00:00",
                dateTimeTo: "2023-01-02T00:00:00",
                customerId: "",
                isFlagged: false
            }
        })
    });

    test('fetch and display messages when a flag is selected', async () => {
        const wrapper = mount(Filterchat, {
            global: {
                mocks: {
                    $route: {
                        query: {}
                    }
                }
            }
        })
        await wrapper.vm.$nextTick()

        // set the flag to true
        wrapper.vm.checked = true;

        const mockData = [
            [1, "SN123", "Hello", "2023-01-01T00:00:00", 1, "User1", "User1"],
        ];

        const _useFetch = vi.fn().mockImplementation((url: string) => {
            return {data: ref(mockData)};
        });

        vi.stubGlobal('useFetch', _useFetch);

        await wrapper.vm.$nextTick()
        await wrapper.vm.$nextTick()
        
        // find the messages that are displayed
        const messages = wrapper.findAll('.searchedMessage')

        // test that the correct number of messages are displayed
        expect(messages.length).toBe(1);
        const firstMessage = messages[0];
        expect(firstMessage.find('.messagesender').text()).toBe('User1:');
        expect(firstMessage.find('.messageContent').text()).toBe('Hello');
        expect(firstMessage.find('.dateTime').text()).toBe('1/1/2023, 12:00:00 AM');
        expect(firstMessage.find('.flagged').exists()).toBe(true);
        
        // test that the correct url is called with the correct parameters
        expect(_useFetch).toHaveBeenCalledWith('http://localhost:8080/search/fulltext', { 
            query: { 
                keywords: "",
                username: "",
                dateTimeFrom: null,
                dateTimeTo: null,
                customerId: "",
                isFlagged: true
            }
        })
    });

    test('fetch and display messages when multiple filters are selected', async () => {
        const wrapper = mount(Filterchat, {
            global: {
                mocks: {
                    $route: {
                        query: {}
                    }
                }
            }
        })
        await wrapper.vm.$nextTick()

        // set the flag to true
        wrapper.vm.checked = true;

        wrapper.setProps({
            keywordArray: [
                { word: 'Hello', isUser: false, isCustomerId: false },
                { word: '@user1', isUser: true, isCustomerId: false },
                { word: 'test', isUser: false, isCustomerId: false}
            ],
            dateTimeObject: {
                dateTimeFrom: "2023-01-01T00:00:00",
                dateTimeTo: "2023-01-02T00:00:00"
            }
        })

        const mockData = [
            [1, "SN123", "Hello this is a test", "2023-01-01T00:00:00", 1, "User1", "User1"],
        ];

        const _useFetch = vi.fn().mockImplementation((url: string) => {
            return {data: ref(mockData)};
        });

        vi.stubGlobal('useFetch', _useFetch);

        await wrapper.vm.$nextTick()
        await wrapper.vm.$nextTick()

        // find the messages that are displayed
        const messages = wrapper.findAll('.searchedMessage')

        // test that the correct number of messages are displayed
        expect(messages.length).toBe(1);
        const firstMessage = messages[0];
        expect(firstMessage.find('.messagesender').text()).toBe('User1:');
        expect(firstMessage.find('.messageContent').text()).toBe('Hello this is a test');
        expect(firstMessage.find('.dateTime').text()).toBe('1/1/2023, 12:00:00 AM');
        expect(firstMessage.find('.flagged').exists()).toBe(true);
        
        // test that the correct url is called with the correct parameters
        expect(_useFetch).toHaveBeenCalledWith('http://localhost:8080/search/fulltext', { 
            query: { 
                keywords: "Hello,test",
                username: "user1",
                dateTimeFrom: "2023-01-01T00:00:00",
                dateTimeTo: "2023-01-02T00:00:00",
                customerId: "",
                isFlagged: true
            }
        })
    });
});