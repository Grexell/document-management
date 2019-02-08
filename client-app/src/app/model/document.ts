import {Access} from './access';

export class Document {
  public id: number = null;
  public name: string;
  public owner: string;
  public description: string;
  public title: string;
  public creator: string;
  public accesses: Access[];
  public signList: any[];
  public needSignList: any[];
  public acceptDate: Date;
  public createDate: Date;
  public validTo: Date;
  public archived: boolean;


  constructor() {
    this.accesses = [];
    this.signList = [];
    this.needSignList = [];
  }
}
