import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { BankAccountFaService } from '../service/bank-account-fa.service';

import { BankAccountFaComponent } from './bank-account-fa.component';

describe('Component Tests', () => {
  describe('BankAccountFa Management Component', () => {
    let comp: BankAccountFaComponent;
    let fixture: ComponentFixture<BankAccountFaComponent>;
    let service: BankAccountFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BankAccountFaComponent],
      })
        .overrideTemplate(BankAccountFaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BankAccountFaComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(BankAccountFaService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.bankAccounts?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
