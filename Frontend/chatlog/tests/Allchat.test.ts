import { afterAll, afterEach, beforeAll, describe, expect, test, vi } from 'vitest'
import { mount, VueWrapper } from '@vue/test-utils'
import Allchat from '../components/Allchat.vue'
import { ref } from 'vue'
import { Server } from 'mock-socket';

interface Message {
    id: number;
    customerId: string;
    dateTime: string;
    text: string;
    isFlagged: number;
    ogUsername: string;
    username: string;
    description?: string;
  }
  
  interface flagWord {
    id: number;
    word: string;
    description: string;
  }

describe('Allchat', async () => {

    afterEach(() => {
        vi.unstubAllGlobals();
    });

    let wrapper: VueWrapper<any>;

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
        let server: Server;
        let socket: WebSocket | undefined;

        beforeAll(() => {
            // Create a mock WebSocket server
            server = new Server('ws://localhost:8080/websocket');

            // Create a mock WebSocket client
            socket = new WebSocket('ws://localhost:8080/websocket');
        });

        afterAll(() => {
            // Close the mock WebSocket client
            socket?.close();

            // Clean up the mock WebSocket server
            server.stop();
        });

        test('should establish WebSocket connection and handle onopen event', async () => {
            
            // Add a delay before asserting the readyState property
            await new Promise((resolve) => setTimeout(resolve, 1000));
        
            // Assert that WebSocket connection is established
            expect(socket?.readyState).toBe(1);
        });

        test('should handle incoming messages if chat is live', async () => {
            console.log('test');
            // Mount the component
            const wrapper = mount(Allchat);

            // Listen for emitted events from the mock server
            server.on('newMessage', (message) => {
                console.log('Received message:', message);
            });


            // Simulate incoming message
            const messageData = {
            event: 'newMessage',
            data: {
                customerId: 'SN123',
                messageText: 'Test message',
                dateTime: '2022-01-01 00:00:00',
                isFlagged: null,
                ogusername: 'user1',
                username: 'user2',
                description: 'Test description'
            }
            };

            // Emit the simulated message from the mock WebSocket server
            server.emit('newMessage', JSON.stringify(messageData));

            

            // Wait for the next tick to allow the component to handle the message
            wrapper.vm.$nextTick();
            wrapper.vm.$nextTick();

            // Assert that the new message is added to the messages array
            expect(wrapper.vm.chatLive).toBe(true);
            expect(wrapper.vm.messages.length).toBe(1);
            expect(wrapper.vm.$data.messages[0].id).toBe(-1);
            expect(wrapper.vm.$data.messages[0].customerId).toBe('123');
            expect(wrapper.vm.$data.messages[0].text).toBe('Test message');
            expect(wrapper.vm.$data.messages[0].dateTime).toBe('2022-01-01');
            expect(wrapper.vm.$data.messages[0].isFlagged).toBe(null);
            expect(wrapper.vm.$data.messages[0].ogUsername).toBe('user1');
            expect(wrapper.vm.$data.messages[0].username).toBe('user2');
            expect(wrapper.vm.$data.messages[0].description).toBe('Test description');
        });
    });
})