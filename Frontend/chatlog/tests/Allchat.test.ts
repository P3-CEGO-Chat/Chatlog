
import { fileURLToPath } from 'node:url'
import { afterEach, describe, expect, test, vi } from 'vitest'
import { mount, VueWrapper } from '@vue/test-utils'
import Allchat from '../components/Allchat.vue'
import { ref } from 'vue'

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

   /*  test('renders the component', async () => {
        const wrapper = mount(Allchat);
        expect(wrapper.exists()).toBe(true);
      }); */

    test('fetches and displays messages correctly', async () => {
        // Mock the server response
        const mockData = JSON.stringify([
          {
            id: 1,
            customerId: '123',
            dateTime: '2022-01-01T00:00:00',
            text: 'Hello',
            isFlagged: 0,
            ogUsername: 'User1',
            username: 'User1',
          },
          {
            id: 2,
            customerId: '456',
            dateTime: '2022-01-02T00:00:00',
            text: 'Hi',
            isFlagged: 1,
            ogUsername: 'User2',
            username: 'User2',
            description: 'Flagged message',
          },
        ]);

        vi.stubGlobal('useFetch', () => ({ data: ref(mockData)}));

        // Mount the component
        const wrapper = mount(Allchat);
    
        // Wait for the next tick to allow the component to fetch and render the messages
        await wrapper.vm.$nextTick();
    
        const test = wrapper.find('.header');
        console.log("Hey1 ",test.text());

        /* const test2 = wrapper.find('.messageBox');
        console.log(test2); */

        console.log(wrapper.vm.messages);

        // Assert that the messages are rendered correctly
        const messages = wrapper.findAll('.messageBox');
        console.log(messages);
        expect(messages.length).toBe(2);
    
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
        expect(secondMessage.find('.flaggedText').text()).toBe('Flagged message');
    });
})