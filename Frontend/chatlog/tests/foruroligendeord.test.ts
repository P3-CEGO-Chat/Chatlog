import { afterAll, afterEach, beforeAll, describe, expect, test, vi, type Mock, beforeEach } from 'vitest'
import { mount, VueWrapper } from '@vue/test-utils'
import foruroligendeord from '../pages/foruroligende-ord.vue'
import { ref } from 'vue'

describe('Foruroligende ord', () => {
    
    afterEach(() => {
        vi.unstubAllGlobals();
    });

    test('fetch and display no flags when none exist', async () => {

        const _fetch = vi.fn().mockImplementation(() => {
            return Promise.resolve([]);
        });

        vi.stubGlobal('$fetch', _fetch);

        const wrapper = mount(foruroligendeord);

        await wrapper.vm.$nextTick();
        await wrapper.vm.$nextTick();
        await wrapper.vm.$nextTick();
        await wrapper.vm.$nextTick();

        const flags = wrapper.findAll('.flag');

        expect(wrapper.vm.flags.length).toBe(0);
        expect(flags.length).toBe(0);
    
    });
    test('fetch and display flags', async () => {
        const mockData = [
            { id: 1, word: 'testord', description: "test" },
            { id: 2, word: 'testord2', description: "test2" }
        ]

        const _fetch = vi.fn().mockImplementation(() => {
            return Promise.resolve(mockData);
        });

        vi.stubGlobal('$fetch', _fetch);

        const wrapper = mount(foruroligendeord);

        await wrapper.vm.$nextTick();
        await wrapper.vm.$nextTick();
        await wrapper.vm.$nextTick();
        await wrapper.vm.$nextTick();

        const flagsvar = wrapper.vm.flags;
        
        expect(flagsvar.length).toBe(2);

        const flagvar1 = flagsvar[0];
        expect(flagvar1.word).toBe('testord');
        expect(flagvar1.description).toBe('test');

        const flagvar2 = flagsvar[1];
        expect(flagvar2.word).toBe('testord2');
        expect(flagvar2.description).toBe('test2');

        const flags = wrapper.findAll('.flag');

        expect(flags.length).toBe(2);

        const flag1 = flags[0];
        expect(flag1.find('h3').text()).toBe('testord');
        expect(flag1.find('p').text()).toBe('test');

        const flag2 = flags[1];
        expect(flag2.find('h3').text()).toBe('testord2');
        expect(flag2.find('p').text()).toBe('test2');
    });
});