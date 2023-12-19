import { afterEach, describe, expect, test, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import foruroligendeord from '../pages/foruroligende-ord.vue'

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

        const flagsvariable = wrapper.vm.flags;
        
        expect(flagsvariable.length).toBe(2);

        const flagvar1 = flagsvariable[0];
        expect(flagvar1.word).toBe('testord');
        expect(flagvar1.description).toBe('test');

        const flagvar2 = flagsvariable[1];
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

    test('should add and display new flag', async () => {
        const mockData = [
            { id: 1, word: 'testord', description: "test" },
            { id: 2, word: 'testord2', description: "test2" }
        ]

        const mockDataResponse = [
            { id: 1, word: 'testord', description: "test" },
        ]

        const _fetch = vi.fn()
        .mockImplementationOnce(() => {
            return Promise.resolve(mockData);
        })
        .mockImplementationOnce(() => {
            return Promise.resolve(mockDataResponse);
        });

        vi.stubGlobal('$fetch', _fetch);

        const wrapper = mount(foruroligendeord);

        await wrapper.vm.$nextTick();
        await wrapper.vm.$nextTick();
        await wrapper.vm.$nextTick();
        await wrapper.vm.$nextTick();

        const flagsvariable = wrapper.vm.flags;

        expect(flagsvariable.length).toBe(2);


        wrapper.vm.newFlag = { word: 'testord3', description: 'test3' };

        await wrapper.vm.postFlag();

        expect(_fetch).toHaveBeenCalledTimes(2);

        expect(_fetch).toHaveBeenCalledWith('http://localhost:8080/flags/addflag', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ word: 'testord3', description: 'test3' })
        });

        expect(flagsvariable.length).toBe(3);
        
        const flags = wrapper.findAll('.flag');

        expect(flags.length).toBe(3);

        const flag3 = flags[2];
        expect(flag3.find('h3').text()).toBe('testord3');
        expect(flag3.find('p').text()).toBe('test3');
    });

    test('should remove and display flag', async () => {
        const mockData = [
            { id: 1, word: 'testord', description: "test" },
            { id: 2, word: 'testord2', description: "test2" }
        ]

        const _fetch = vi.fn()
        .mockImplementationOnce(() => {
            return Promise.resolve(mockData);
        })
        .mockImplementationOnce(() => {
            return Promise.resolve(null);
        });

        vi.stubGlobal('$fetch', _fetch);

        const wrapper = mount(foruroligendeord);

        await wrapper.vm.$nextTick();
        await wrapper.vm.$nextTick();
        await wrapper.vm.$nextTick();
        await wrapper.vm.$nextTick();

        expect(wrapper.vm.flags.length).toBe(2);

        await wrapper.vm.removeFlag('testord2');

        expect(_fetch).toHaveBeenCalledTimes(2);

        expect(_fetch).toHaveBeenCalledWith('http://localhost:8080/flags/removeflag?word=testord2');

        expect(wrapper.vm.flags.length).toBe(1);
        
        const flags = wrapper.findAll('.flag');

        expect(flags.length).toBe(1);
    });

    test('should edit and display flag', async () => {
        const mockData = [
            { id: 1, word: 'testord', description: "test" },
            { id: 2, word: 'testord2', description: "test2" }
        ]

        const _fetch = vi.fn()
        .mockImplementationOnce(() => {
            return Promise.resolve(mockData);
        })
        .mockImplementationOnce(() => {
            return Promise.resolve(null);
        });

        vi.stubGlobal('$fetch', _fetch);

        const wrapper = mount(foruroligendeord);

        await wrapper.vm.$nextTick();
        await wrapper.vm.$nextTick();
        await wrapper.vm.$nextTick();
        await wrapper.vm.$nextTick();

        expect(wrapper.vm.flags.length).toBe(2);

        await wrapper.vm.editFlag(1, 'testord3', 'test3');

        expect(_fetch).toHaveBeenCalledTimes(2);

        expect(_fetch).toHaveBeenCalledWith('http://localhost:8080/flags/updateflag', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ id: 1, word: 'testord3', description: 'test3' })
        
        });

        wrapper.vm.flags[0].word = 'testord3';
        wrapper.vm.flags[0].description = 'test3';

        wrapper.vm.modal2Open = false;

        await wrapper.vm.$nextTick();
        await wrapper.vm.$nextTick();
        await wrapper.vm.$nextTick();

        expect(wrapper.vm.flags.length).toBe(2);
        
        const flags = wrapper.findAll('.flag');

        expect(flags.length).toBe(2);

        const flag3 = flags[0];
        expect(flag3.find('h3').text()).toBe('testord3');
        expect(flag3.find('p').text()).toBe('test3');

        const flag2 = flags[1];
        expect(flag2.find('h3').text()).toBe('testord2');
        expect(flag2.find('p').text()).toBe('test2');
    });
});