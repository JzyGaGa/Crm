package cn.itheima.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.itcast.utils.Page;
import cn.itheima.pojo.BaseDict;
import cn.itheima.pojo.Customer;
import cn.itheima.pojo.QueryVo;
import cn.itheima.service.CustomerService;

@Controller
@RequestMapping("/customer")
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	/**
	 * 资源数据注入
	 */
	
	@Value("${customer.dic.source}")
	private String source;
	@Value("${customer.dic.industry}")
	private String industry;
	@Value("${customer.dic.level}")
	private String level;
	
	
	@RequestMapping("/list")
	public String list(QueryVo vo,Model model)throws Exception{
		List<BaseDict> resorceList = customerService.findDictByCode(source);
		List<BaseDict> industryList = customerService.findDictByCode(industry);
		List<BaseDict> levelList = customerService.findDictByCode(level);
		//将数据类型转化成utf-8
		if(vo.getCustName()!=null) {
			vo.setCustName(new String(vo.getCustName().getBytes("iso8859-1"),"utf-8"));
		}
		
		
		if(vo.getPage()==null)
			vo.setPage(1);
		//设置查询其实条数
		vo.setStart((vo.getPage()-1)*vo.getSize());
		//查寻后应该显示的数据
		Integer count = customerService.findCountByVo(vo);
	
		
		System.out.println(count);
		List<Customer> resList = customerService.findCustomerByVo(vo);
		Page<Customer> page = new Page<Customer>();
		page.setPage(vo.getPage()); //设置初始的页数
		page.setRows(resList); //存储数据的集合
		page.setSize(vo.getSize());  //每页显示的条数
		page.setTotal(count);   //数据总数
		
		
		model.addAttribute("page", page);
		//查询下拉表的的数据
		model.addAttribute("fromType", resorceList);
		model.addAttribute("industryType", industryList);
		model.addAttribute("levelType", levelList);
		//设置数据回显
		model.addAttribute("custName", vo.getCustName());
		model.addAttribute("custSource", vo.getCustSource());
		model.addAttribute("custIndustry", vo.getCustIndustry());
		model.addAttribute("custLevel", vo.getCustLevel());
		return "customer";
		
	}
	
	@RequestMapping("/detail")
	@ResponseBody()
	public Customer detail(Long id)throws Exception{
		Customer customer = customerService.findCustomerById(id);
		return customer;
	}
	
	@RequestMapping("/update")
	public String update(Customer customer)throws Exception{
		customerService.updateCustomerById(customer);
		return "customer";
	}
	
	@RequestMapping("/delete")
	public String delete(Long id)throws Exception{
		customerService.deleteCustomerById(id);
		return "customer";
	}
	
}
