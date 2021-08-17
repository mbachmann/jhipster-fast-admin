import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SignatureFaService } from '../service/signature-fa.service';

import { SignatureFaComponent } from './signature-fa.component';

describe('Component Tests', () => {
  describe('SignatureFa Management Component', () => {
    let comp: SignatureFaComponent;
    let fixture: ComponentFixture<SignatureFaComponent>;
    let service: SignatureFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SignatureFaComponent],
      })
        .overrideTemplate(SignatureFaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SignatureFaComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(SignatureFaService);

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
      expect(comp.signatures?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
