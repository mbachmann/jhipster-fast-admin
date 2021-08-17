import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ValueAddedTaxDetailComponent } from './value-added-tax-detail.component';

describe('Component Tests', () => {
  describe('ValueAddedTax Management Detail Component', () => {
    let comp: ValueAddedTaxDetailComponent;
    let fixture: ComponentFixture<ValueAddedTaxDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ValueAddedTaxDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ valueAddedTax: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ValueAddedTaxDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ValueAddedTaxDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load valueAddedTax on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.valueAddedTax).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
