import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BankAccountFaDetailComponent } from './bank-account-fa-detail.component';

describe('Component Tests', () => {
  describe('BankAccountFa Management Detail Component', () => {
    let comp: BankAccountFaDetailComponent;
    let fixture: ComponentFixture<BankAccountFaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [BankAccountFaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ bankAccount: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(BankAccountFaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BankAccountFaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load bankAccount on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.bankAccount).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
