import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CustomFieldValueDetailComponent } from './custom-field-value-detail.component';

describe('Component Tests', () => {
  describe('CustomFieldValue Management Detail Component', () => {
    let comp: CustomFieldValueDetailComponent;
    let fixture: ComponentFixture<CustomFieldValueDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CustomFieldValueDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ customFieldValue: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CustomFieldValueDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CustomFieldValueDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load customFieldValue on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.customFieldValue).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
