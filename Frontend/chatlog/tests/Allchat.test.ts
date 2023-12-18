import { afterAll, afterEach, beforeAll, describe, expect, test, vi, type Mock, beforeEach } from 'vitest'
import { mount, VueWrapper } from '@vue/test-utils'
import Allchat from '../components/Allchat.vue'
import { ref } from 'vue'
import { Server as MockServer } from 'mock-socket';

describe('Allchat', async () => {

    afterEach(() => {
        vi.unstubAllGlobals();
    });

    describe('fetchMessages', () => {
        test('fetches and displays no messages when the server response is empty', async () => {
            // Mock the server response
            const mockData = JSON.stringify([]);

            vi.stubGlobal('useFetch', () => ({ data: ref(mockData)}));

            // Mount the component
            const wrapper = mount(Allchat);

            // Wait for the next tick to allow the component to fetch and render the messages
            await wrapper.vm.$nextTick();
            await wrapper.vm.$nextTick();
            await wrapper.vm.$nextTick();
            await wrapper.vm.$nextTick();

            // Assert that no messages are rendered
            const messages = wrapper.findAll('.messageBox');
            expect(messages.length).toBe(0);
        });

        test('fetches and displays messages correctly when there are multiple users', async () => {
            // Mock the server response
            const mockData = JSON.stringify([
                [1, '123', 'Hello', '2022-01-01T00:00:00', 0, 'User1', 'User1'],
                [2, '456', 'Hi', '2022-01-02T00:00:00', 1, 'User2', 'User2'],
                [3, '789', 'Hey', '2022-01-03T00:00:00', 0, 'User3', 'User3']]);

            vi.stubGlobal('useFetch', () => ({ data: ref(mockData)}));

            // Mount the component
            const wrapper = mount(Allchat);

            // Wait for the next tick to allow the component to fetch and render the messages
            await wrapper.vm.$nextTick();
            await wrapper.vm.$nextTick();
            await wrapper.vm.$nextTick();
            await wrapper.vm.$nextTick();

            // Assert that the messages are rendered correctly
            const messages = wrapper.findAll('.messageBox');
            expect(messages.length).toBe(3);

            const firstMessage = messages[0];
            expect(firstMessage.find('.username').text()).toBe('User1:');
            expect(firstMessage.find('.messageContent').text()).toBe('Hello');
            expect(firstMessage.find('.Time').text()).toBe('1/1/2022, 12:00:00 AM');
            expect(firstMessage.find('.flagged').exists()).toBe(false);

            const secondMessage = messages[1];
            expect(secondMessage.find('.username').text()).toBe('User2:');
            expect(secondMessage.find('.messageContent').text()).toBe('Hi');
            expect(secondMessage.find('.Time').text()).toBe('1/2/2022, 12:00:00 AM');
            expect(secondMessage.find('.flagged').exists()).toBe(true);
            expect(secondMessage.find('.flaggedText').text()).toBe('Ukendt grund');

            const thirdMessage = messages[2];
            expect(thirdMessage.find('.username').text()).toBe('User3:');
            expect(thirdMessage.find('.messageContent').text()).toBe('Hey');
            expect(thirdMessage.find('.Time').text()).toBe('1/3/2022, 12:00:00 AM');
            expect(thirdMessage.find('.flagged').exists()).toBe(false);
        });
    });

    // Websocket tests
    describe('Websocket', () => {
        let mockServer: MockServer;
        let originalWebSocket: typeof WebSocket;
        let webSocketSpy: Mock;

        beforeAll(() => {
            // Create a mock WebSocket server
            mockServer = new MockServer('ws://localhost:8080/websocket');

            // save the original WebSocket
            originalWebSocket = global.WebSocket;

            // Use the original WebSocket URL without modification
            webSocketSpy = vi.fn().mockImplementation((url: string) => {
                return new originalWebSocket(url);
            });


            // Mock the WebSocket constructor
            global.WebSocket = new Proxy(originalWebSocket, {
                construct(target, args) {
                    return webSocketSpy(...args);
                }
            }) as any;

        });

        afterAll(() => {
            // Clean up the mock WebSocket server
            global.WebSocket = originalWebSocket;
            mockServer.stop();

            vi.unstubAllGlobals();
        });

        test('should establish WebSocket connection', async () => {
            // Mount the component
            const wrapper = mount(Allchat);

            // Mock the server response
            const mockData = JSON.stringify([
                [1, '123', 'Hello', '2022-01-01T00:00:00', 0, 'User1', 'User1'],
                [2, '456', 'Hi', '2022-01-02T00:00:00', 1, 'User2', 'User2'],
                [3, '789', 'Hey', '2022-01-03T00:00:00', 0, 'User3', 'User3']]);

            vi.stubGlobal('useFetch', () => ({ data: ref(mockData)}));

            // Wait for all asynchronous operations to complete
            await wrapper.vm.$nextTick();
            await wrapper.vm.$nextTick();

            // Assert that the WebSocket spy was called with the correct URL
            expect(webSocketSpy).toHaveBeenCalledWith('ws://localhost:8080/websocket');
        });

        test('should handle onmessage event', async () => {
            // Mount the component
            const wrapper = mount(Allchat);

            const mockData = JSON.stringify([
                [1, 'SN123', 'Hello', '2022-01-01T00:00:00', 0, 'User1', 'User1'],
                [2, 'SN412', 'Hi', '2022-01-02T00:00:00', 1, 'User2', 'User2'],
                [3, 'SN423', 'Hey', '2022-01-03T00:00:00', 0, 'User3', 'User3']
            ]);

            const mockDataFlag = JSON.stringify([
                [1, 'testord', 'test grund'],
                [2, 'testord2', 'test grund2']
            ]);

            const _useFetch = vi.fn().mockImplementation((url: string) => {
                    return {data: ref(mockData)};
            });

            vi.stubGlobal('useFetch', _useFetch);

            wrapper.vm.getFlaggedData = vi.fn().mockImplementation(() => {
                return new Promise((resolve) => {
                    resolve(mockDataFlag);
                });
            });


            const mockMessage = JSON.stringify({
                event: 'newMessage',
                data: {
                    customerId: 'SN431',
                    messageText: 'Test message',
                    dateTime: '2023-01-01T00:00:00',
                    isFlagged: null,
                    ogusername: 'user1',
                    username: 'user1',
                    description: 'Test'
                }
            });

            // Wait for all asynchronous operations to complete
            await wrapper.vm.$nextTick();
            await wrapper.vm.$nextTick();

            // Mock the server response and send it to the client
            mockServer.on('connection', socket => {
                socket.send(mockMessage);
            });

            // Wait for all asynchronous operations to complete
            await wrapper.vm.$nextTick();
            await wrapper.vm.$nextTick();

            // wait 1 seconds for the websocket to send the message
            await new Promise(resolve => setTimeout(resolve, 1000));

            // Assert that the messages are rendered correctly
            expect(wrapper.vm.messages.length).toBe(4);

            const messages = wrapper.findAll('.messageBox');
            const fourthMessage = messages[3];
            expect(fourthMessage.find('.username').text()).toBe('user1:');
            expect(fourthMessage.find('.messageContent').text()).toBe('Test message');
            expect(fourthMessage.find('.Time').text()).toBe('1/1/2023, 12:00:00 AM');
            expect(fourthMessage.find('.flagged').exists()).toBe(false);
        });

        test('Should post message to slack', async () => {
            // Mount the component
            const wrapper = mount(Allchat);

            const mockData = JSON.stringify([
                [1, 'SN123', 'Hello', '2022-01-01T00:00:00', 0, 'User1', 'User1'],
                [2, 'SN412', 'Hi', '2022-01-02T00:00:00', 1, 'User2', 'User2'],
                [3, 'SN423', 'Hey', '2022-01-03T00:00:00', 0, 'User3', 'User3']
            ]);

            const mockDataFlag = JSON.stringify([
                [1, 'testord', 'test grund'],
                [2, 'testord2', 'test grund2']
            ]);

            const _useFetch = vi.fn().mockImplementation((url: string) => {
                return {data: ref(mockData)};
            });

            vi.stubGlobal('useFetch', _useFetch);

            const _fetch = vi.fn();

            vi.stubGlobal('$fetch', _fetch);

            wrapper.vm.getFlaggedData = vi.fn().mockImplementation(() => {
                return new Promise((resolve) => {
                    resolve(mockDataFlag);
                });
            });


            const mockMessage = JSON.stringify({
                event: 'newMessage',
                data: {
                    customerId: 'SN431',
                    messageText: 'Test message',
                    dateTime: '2023-01-01T00:00:00',
                    isFlagged: 1,
                    ogusername: 'user1',
                    username: 'user1',
                    description: 'Test'
                }
            });

            // Wait for all asynchronous operations to complete
            await wrapper.vm.$nextTick();
            await wrapper.vm.$nextTick();

            // Mock the server response and send it to the client
            mockServer.on('connection', socket => {
                socket.send(mockMessage);
            });

            // wait 1 seconds for the websocket to send the message
            await new Promise(resolve => setTimeout(resolve, 1000));

            const message = { text: "(Flagged) " + "Skrevet af user1: Test message" + "\nGrund: \n" + "http://localhost:3000/?messageid=undefined"};

            expect(_fetch).toHaveBeenCalledTimes(1);
            expect(_fetch).toHaveBeenCalledWith(`http://localhost:8080/api/sendtoslack`, {
                method: 'POST',
                headers: {
                  'Content-Type': 'application/json',
                },
                body: JSON.stringify(message),
              })

        });
    });
})