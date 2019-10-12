遗传算法，顾名思义，是在计算机中模拟自然界中的进化过程，优胜劣汰，从而得到出最优个体的算法。因此我们可以从自然界的进化过程着手，来理解什么是遗传算法。

        在自然界中，物种的进化往往是以外界环境的变化为开端的，比如水中生物用于水下呼吸的鳃，南极生物皮下厚厚的脂肪，都是适应外界环境的结果。而在遗传算法中，算法想要达到的目的就是这里的“外界环境”，于是我们设置了一个数值来评估每个个体与外界环境的契合程度，称为“适应度（Fitness）”。比如你想要求一个函数的最大值，那么函数值的大小就是其适应度；你想求两地之间的最短路线，那么距离就是其适应度。

        了解过进化过程的人都知道，进化本质上是一个“筛选”的过程，基因随机产生变异，然后携带劣质基因的个体由于不能适应环境而被淘汰，如此循环很多代之后，留存下来的都会是适应度很高的个体。这个优胜劣汰的过程，在遗传算法中称为“选择（Select）”。

        自然界的种群，是靠繁殖来补充在选择过程中的淘汰的个体，在繁殖的过程中，父母的染色体重新组合，使得子女会带有父母的部分基因，更有一定可能会集成父母二者的优势。遗传算法中，随机选取两个个体，让二者的染色体随机重组生成一条或多条新染色体的过程，称为“交叉（Cross）”。

        除了交叉之外，还有一个提升基因多样性的重要手段，就是“变异（Mutation）”，在算法中表现为将某一位进行反转，来模拟自然界中的基因突变。

        遗传算通过选择，交叉，变异操作对种群进行不断迭代，就会使得整个群体的适应度越来越高，使其中的最优个体不断的逼近最优解。但是想要完成一个完整的遗传算法，还需要一些其它的组成部分，接下来结合代码进行说明。

注：个体&染色体：本文假定每个个体只含有一个染色体，因此“染色体”和“个体”均表示基因的载体，在阅读中二者可以认为是同一种东西。



遗传算法解决函数优化问题实例：



适应度：要求函数的最小值，那么就要使得到结果越小的个体，适应度越高。

精度：想要精确到小数点后6位，那么定义域的区间至少需要划分成6*10^6等份，再求出表示这个量级所需要的二进制位数，作为基因的位数

终止条件：通常是达到指定迭代次数和达到指定精度便停止迭代，输出结果。本文代码为了便于观察只设置了迭代次数作为终止条件。

为了便于以后修改，我将每一个步骤都写成了单独的类。



1.编码和解码

编码：初始化，将染色体的每一位都随机置为0或1

//初始化群体（编码）
public class ClsInit {
	
	//初始化一条染色体
	public String initSingle(int GENE){
		String res = "";
		for(int i = 0; i < GENE; i++){
			if(Math.random() < 0.5){
				res += 0;
			}else{
				res +=1;
			}
		}
		return res;
	}
 
	//初始化一组染色体
	public String[] initAll(int GENE,int groupsize){
		String[] iAll = new String[groupsize];
		for(int i = 0; i < groupsize; i++){
			iAll[i] = initSingle(GENE);
		}
		return iAll;
	}
}
解码时，先将一条染色体中表示x的部分和表示y的部分拆分开来，再把二进制转换为10进制。

//解码
public class ClsDecode {
	//单个染色体解码
	public double[] decode(String single,int GENE){
		//二进制数前GENE/2位为x的二进制字符串，后GENE/2位为y的二进制字符串
		int a=Integer.parseInt(single.substring(0,GENE/2),2);
		int b=Integer.parseInt(single.substring(GENE/2,GENE),2);
		double[] x = {-1,-1};//new double[2];
		x[0] = a * (6.0 - 0) / (Math.pow(2, GENE/2) - 1);	//x的基因
		x[1] = b * (6.0 - 0) / (Math.pow(2, GENE/2) - 1);	//y的基因
		
		return x;
	}
}


2.计算适应度

达到的效果是是输入群体数组，然后返回对应的适应度数组。

//适应度
import java.lang.Math;
public class ClsFitness {
	//计算个体的适应度
	public double fitSingle(String str,int GENE){
		ClsDecode decode = new ClsDecode();
		double[] x =decode.decode(str,GENE);
		
		//适应度计算公式
		//问题：如果计算出来有负有正该怎么处理？
		double fitness = Math.sin(2 * x[0]) * Math.sin(2 * x[0]) 
				+ Math.sin(2 * x[1]) * Math.sin(2 * x[1]);	//sin+sin越大，3-sin-sin越小，即得到的值越小个体的适应度就越大
		return fitness;
	}
 
	//批量计算数组的适应度
	public double[] fitAll(String str[],int GENE){
		double [] fit = new double[str.length];
		for(int i = 0;i < str.length; i++){
			fit[i] = fitSingle(str[i],GENE);
		}
		return fit;
	}
	
	//适应度最值（返回序号）
	public int mFitNum(double fit[]){
		double m = fit[0]; 
		int n = 0;
		for(int i = 0;i < fit.length; i++){
			if(fit[i] > m){
				//最大值
				m = fit[i];
				n = i;
			}
		}
		return n;
	}
	
	
	
	//适应度最值（返回适应度）
	public double mFitVal(double fit[]){
		double m = fit[0]; 
		for(int i = 0;i < fit.length; i++){
			if(fit[i] > m){
				//最大值
				m = fit[i];
			}
		}
		return m;
	}
}


3.选择

基本思想：适应度越大的个体，被选中保留到下一代的可能性越高。使用轮盘赌算法可以达到这个目的。

为了保持群体总数不变，被淘汰的个体由随机生成的新个体补充。

//轮盘赌选择
public class ClsRWS {
	ClsInit init = new ClsInit();
	ClsFitness fitness = new ClsFitness();
	
	/* fit[]适应度数组
	 * group[]群体数组
	 * GENE基因数
	 * */
	public String[] RWS(String group[], int GENE){
		double p[] = new double[group.length];	//染色体概率数组
		String[] newgroup = new String[group.length];
		double F = 0;	//适应度的和
		
		double[] fit = fitness.fitAll(group,GENE);	//计算适应度数组
		
		//求适应度的和F
		for(int i = 0; i < fit.length; i++){
			F = F +fit[i]; 			
		}
		
		//初始化p[]
		for(int i = 0; i < fit.length; i++){
			p[i] = fit[i] / F;
		}
		
		//求出适应度最大的个体maxStr,它的序号是max
		int max = fitness.mFitNum(fit);
		String maxStr = group[max];
		
		//转动轮盘，适应度大的被选中的概率大
		for (int i = 0; i < fit.length; i++){
			double r = Math.random();
			double q= 0;	//累计概率
			
			//适应度最大的个体直接继承
			if(i == max){
				newgroup[i] = maxStr;	
				p[i] = 0;	//将其概率置空	
				//System.out.println("继承的最大适应度为"+fit[i]);
				continue;
			}
 
			//遍历轮盘，寻找轮盘指针指在哪个区域
			for(int j = 0; j < fit.length; j++){
				q += p[j];
				if(r <= q){	
					newgroup[i] = group[j];	//如果被选中，保留进下一代
					p[j] = 0;	//将其概率置空					
					break;
				}	
				newgroup[i] = init.initSingle(GENE);	//如果没被选中，被外来者取代
			}
		}
		return newgroup;
	}
}

4.交叉
染色体依次两两配对，随机在一对染色体上选取一点分成两段，然后互换重组为新的两条染色体。

（在交叉这一步，有更好的策略是只选取选择得到的适应度高的个体进行交叉，并且可以选择交叉后保留原个体）

//交叉
public class ClsCross {
	ClsFitness fitness = new ClsFitness();
	//group群体
	//GENE基因数
	//mFitNum最大适应度染色体序号
	public String[] cross(String[] group,int GENE,double crossRate){
		String temp1, temp2;
		int pos = 0;
		
		double[] fit = fitness.fitAll(group,GENE);
		int mFitNum = fitness.mFitNum(fit);	//计算适应度最大的染色体序号
		String max = group[mFitNum];
		
		for(int i = 0; i < group.length; i++){
			if(Math.random() < crossRate){
				pos = (int)(Math.random()*GENE) + 1;	//交叉点
				temp1 = group[i].substring(0, pos) + group[(i+1) % group.length].substring(pos);	//%用来防止数组越界
				temp2 = group[(i+1) % group.length].substring(0, pos) + group[i].substring(pos);
				group[i] = temp1;
				group[(i+1) % group.length] = temp2;
			}
		}
		group[0] = max;
		return group;
	}
}


5.变异

在染色体上随机选取一位，翻转其二进制位

//变异
public class ClsMutation {
	//替换String中的指定位
	//str要改动的字符串
	//num要改动的位（从0开始数）
	//pos要把这一位改动成什么
	public String replacePos(String str,int num,String pos){
		String temp;
		if(num == 0){
			temp = pos + str.substring(1);
		}else if(num == str.length()-1){
			temp = str.substring(0, str.length() - 1) + pos;
		}else{
			String temp1 = str.substring(0, num);
			String temp2 = str.substring(num + 1);
			temp = temp1 + pos + temp2;
		}
		return temp;		
	}
	
	//MP = Mutation probability变异概率
	public String[] mutation(String[] group,int GENE,double MP){
		ClsFitness fitness = new ClsFitness();
		double[] fit = fitness.fitAll(group,GENE);
		int mFitNum = fitness.mFitNum(fit);	//计算适应度最大的染色体序号
		String max = group[mFitNum];
		
		for(int i = 0; i < group.length * MP; i++){
			int n = (int) (Math.random() * GENE * group.length );	//从[0，GENE * group.length)区间取随机数
			int chrNum = (int) (n / GENE);	//取得的染色体数组下标
			int gNum = (int)(n % GENE); 	//取得的基因下标
			String temp = "";
			
			if(group[chrNum].charAt(gNum) == '0' ){
				temp = replacePos(group[chrNum], gNum, "1");
			}else{
				temp = replacePos(group[chrNum], gNum, "0");
			}
			group[chrNum] = temp;
		}
		group[0] = max;
		return group;
	}
}

main方法：

我在选择，交叉和变异后都输出了当前群体的最大适应度，以便观察三种操作对于适应度的影响。

public class GAmain {
	public static final int groupsize = 10;	//染色体数（群体中个体数）
	public static final double MP = 0.15;	//变异概率
	public static final double CP = 0.6;	//交叉概率
	public static final int ITERA = 1000;	//迭代次数
	public static final int accuracy = 8;	//精确度，选择精确到小数点后几位
	
	
	//求出精度对应的所需基因数
	int temp = (int) ((int)Math.log(6)+ accuracy*Math.log(10) );
	int GENE = temp * 2;	//基因数
	
	
	//输出原群体和适应度，测试用
	public void outAll(String[] group){
		ClsFitness fitness = new ClsFitness();
		System.out.println("原群体");		
		for(String str:group){
			System.out.println(str);
		}	
 
		double fit[] = fitness.fitAll(group,GENE);
		System.out.println("原群体适应度");
		for(double str:fit){
			System.out.println(str);
		}
	}
	
	//输出适应度最大值,以及返回最优的个体,测试用
	public int outMax(String str,String[] group){
		ClsFitness fitness = new ClsFitness();
		double[] fit = fitness.fitAll(group,GENE);
		double max = fitness.mFitVal(fit);
		System.out.println(str+"后适应度最大值为"+max);
		return fitness.mFitNum(fit);
	}
 
	public static void main(String[] args) {
		ClsInit init = new ClsInit();
		ClsFitness fitness = new ClsFitness();
		ClsRWS rws = new ClsRWS();
		ClsCross cross = new ClsCross();
		ClsMutation mutation = new ClsMutation();
		ClsDecode decode = new ClsDecode();
		GAmain ga = new GAmain();
		
		String group[] = init.initAll(ga.GENE,groupsize);	//初始化
		ga.outAll(group);
		
		for(int i = 0; i < ITERA; i++){
			group = rws.RWS(group, ga.GENE); //选择
			ga.outMax("选择",group);
			
			group = cross.cross(group,ga.GENE,CP);	//交叉
			ga.outMax("交叉",group);
			
			group = mutation.mutation(group, ga.GENE, MP);	//变异
			ga.outMax("变异",group);
			
			System.out.println("");
		}
		int max = ga.outMax("完成", group);
		double best[] = decode.decode(group[max], ga.GENE);	//解码
		double result = 3-fitness.fitSingle(group[max], ga.GENE);
		System.out.println("x1 = "+best[0]+"\n"+"x2 = "+best[1]);
		System.out.println("最小值为"+result);
	}
}

运行结果：
