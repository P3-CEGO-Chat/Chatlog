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
            [1, '123', 'Hello', '2022-01-01T00:00:00', 0, 'User1', 'User1'],
            [2, '456', 'Hi', '2022-01-02T00:00:00', 1, 'User2', 'User2']]);

        vi.stubGlobal('useFetch', () => ({ data: ref(mockData)}));

        // Mount the component
        const wrapper = mount(Allchat);
    
        // Wait for the next tick to allow the component to fetch and render the messages
        await wrapper.vm.$nextTick();
        await wrapper.vm.$nextTick();
        await wrapper.vm.$nextTick();
        await wrapper.vm.$nextTick();
    
        const test = wrapper.find('.header');
        console.log("Hey1 ",test.text());

        /* const test2 = wrapper.find('.messageBox');
        console.log(test2); */

        console.log("maybe", wrapper.vm.messages);

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
        expect(secondMessage.find('.flaggedText').text()).toBe('Ukendt grund');
    });
})